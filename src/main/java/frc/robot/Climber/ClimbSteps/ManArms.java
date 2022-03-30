package frc.robot.Climber.ClimbSteps;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Climber.ErrorCommand;
import frc.robot.Climber.CmdClimb.SharedVariables;

public class ManArms extends ErrorCommand {
    
    RobotContainer r;

    SharedVariables sv;
    int currentStage;
    int stage;

    double[] armPositionList = new double[10];
    int idx = -1;

    double startTime;

    double posCheckDelayStart;
    boolean attached;

    double extraTimeStart;
    boolean extraTimeStartSet = false;

    public ManArms(RobotContainer r, SharedVariables sv, int stage, SequentialCommandGroup sCG){
        super(sCG);
        this.r = r;
        this.sv = sv;
        this.stage = stage;
    }

    protected void updatePositionArray(double val){
        idx++;
        if(idx > armPositionList.length){
            idx = 0;
        }
        armPositionList[idx] = val;
    }

    protected double getPosition(int idx){
        int length = armPositionList.length;
        if(idx < 0){
            return armPositionList[length + idx];
        } else if(idx > length){
            return armPositionList[idx - length];
        } else {
            return armPositionList[idx];
        }
    }

    @Override 
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
        posCheckDelayStart = Timer.getFPGATimestamp();
        currentStage = sv.get();
        System.out.println("Stage " + stage + " init");
        System.out.println(currentStage + " " + stage);
        hasReachedThreshold = false;

        r.climb.climbArmL.setBrake(true);
        r.climb.climbArmR.setBrake(true);
    }

    boolean hasReachedThreshold;
    @Override
    public void execute(){
        super.execute();

        if(currentStage <= stage){
            double armSetPoint;

            double winchPos = r.climb.climbWinch.getPosition();
            double winchTarget;
            if(stage == 1){ //unroll the winch further when traversing bars vs climbing from the ground
                winchTarget = r.climb.cals.winchOutRevs;
            } else {
                winchTarget = r.climb.cals.winchOutRevsFar;
            }

            //start by rolling out the winch at full speed
            if(winchPos > winchTarget){
                armSetPoint = r.climb.cals.armLowPoint;

                r.climb.climbWinch.setBrake(false);
                r.climb.driveWinch(r.climb.cals.releaseWinchPower);
            } else {
                //then transition to targeting a specific winch length
                double tgt;
                if(stage == 1){
                    tgt = r.climb.cals.targetWinchOutRevs;
                    armSetPoint = r.climb.cals.armHoldPoint;
                } else {
                    //more extention for traversals, also lower arm positions
                    tgt = r.climb.cals.targetWinchOutRevsFar;
                    armSetPoint = r.climb.cals.armLowHoldPoint;
                }

                //pid to target winch position
                double wErr = tgt - winchPos;
                double pwr = r.climb.cals.winchKp * wErr;
                if(pwr > r.climb.cals.maxWinchPIDpwr) pwr = r.climb.cals.maxWinchPIDpwr;
                else if(pwr < -r.climb.cals.maxWinchPIDpwr) pwr = -r.climb.cals.maxWinchPIDpwr;
                r.climb.driveWinch(pwr);
                r.climb.climbWinch.setBrake(true);
            }

            //pid to target arm position
            double armL = r.climb.climbArmL.getPosition() * 360;
            double armR = r.climb.climbArmR.getPosition() * 360;
            double errorL = armSetPoint - armL;
            double errorR = armSetPoint - armR;
            double pwrL = errorL * r.climb.cals.armHoldKp;
            double pwrR = errorR * r.climb.cals.armHoldKp;
            
            if(Math.abs(errorL + errorR) < 5) hasReachedThreshold = true;

            //slow the arms when they are close to the target
            double maxArmPower;
            if(hasReachedThreshold) maxArmPower = r.climb.cals.armBasePower;
            else if(winchPos < r.climb.cals.winchOutRevs) maxArmPower = r.climb.cals.armPower;
            else maxArmPower = r.climb.cals.armBasePower;
            
            double minPwr = 0;
            if(armSetPoint > 50 && stage != 1) minPwr = 0.1;

            if(pwrL > maxArmPower) pwrL = maxArmPower;
            else if(pwrL < 0) pwrL = minPwr;
            if(pwrR > maxArmPower) pwrR = maxArmPower;
            else if(pwrR < 0) pwrR = minPwr;

            r.climb.driveArms(pwrL, pwrR);
        } else {
            System.out.println("Stage " + stage + " skipped");
        }
    }

    @Override
    public void end(boolean interrupted){
        r.climb.driveArms(0);
        r.climb.driveWinch(0);
        r.climb.climbWinch.setBrake(true);
        if(!interrupted && currentStage == stage){
            sv.set(stage + 1);
        }
        System.out.println("Stage " + stage + " ended");
    }

    @Override
    public boolean isFinished(){
        boolean stoppedMoving = Math.abs(getPosition(idx) - getPosition(idx - r.climb.cals.prevIdxArms)) > r.climb.cals.minRotDiffArms;
        boolean startTimePassed = posCheckDelayStart + r.climb.cals.posCheckDelayArms > Timer.getFPGATimestamp();
        return currentStage > stage || (stoppedMoving && startTimePassed && stage != 1) || r.inputs.gather.get();
    }

    @Override
    public boolean isError(){
        return false; //startTime + r.climb.cals.maxRunTimeArms < Timer.getFPGATimestamp();
    }
}
