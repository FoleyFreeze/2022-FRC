package frc.robot.Climber.ClimbSteps;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Climber.ErrorCommand;
import frc.robot.Climber.CmdClimb.SharedVariables;

public class Winch extends ErrorCommand{

    RobotContainer r;

    SharedVariables sv;
    int currentStage;
    int stage;

    double[] winchPositionList = new double[10];
    int idx = -1;

    double startTime;

    double posCheckDelayStart;
    boolean attached;

    double extraTimeStart;
    boolean extraTimeStartSet = false;

    public Winch(RobotContainer r, SharedVariables sv, int stage, SequentialCommandGroup sCG){
        super(sCG);
        this.r = r;
        this.sv = sv;
        this.stage = stage;
    }

    private void updatePositionArray(double val){
        idx++;
        if(idx >= winchPositionList.length){
            idx = 0;
        }
        winchPositionList[idx] = val;
    }

    private double getPosition(int idx){
        int length = winchPositionList.length;
        if(idx < 0){
            return winchPositionList[length + idx]; 
        } else if(idx > length){
            return winchPositionList[idx - length];
        } else {
            return winchPositionList[idx];
        }
    }

    @Override 
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
        posCheckDelayStart = Timer.getFPGATimestamp();
        currentStage = sv.get();
        System.out.println("Stage " + stage + " init");
        System.out.println(currentStage + " " + stage);
    }

    @Override
    public void execute(){
        super.execute();
        updatePositionArray(r.climb.climbWinch.getPosition());

        if(currentStage <= stage){
            double winchPos = r.climb.climbWinch.getPosition();
            double wErr = 5 - winchPos;
            double pwr = r.climb.cals.winchKp * wErr;
            if(pwr > r.climb.cals.winchPower.get()) pwr = r.climb.cals.winchPower.get();
            else if(pwr < -r.climb.cals.winchPower.get()) pwr = -r.climb.cals.winchPower.get();
            r.climb.driveWinch(pwr);
            
            r.climb.climbArmL.setBrake(false);
            r.climb.climbArmR.setBrake(false);

            if(winchPos > -25) {
                double armL = r.climb.climbArmL.getPosition() * 360;
                double armR = r.climb.climbArmR.getPosition() * 360;
                double errorL = r.climb.cals.armStartPoint - armL;
                double errorR = r.climb.cals.armStartPoint - armR;
                double pwrL = errorL * r.climb.cals.armHoldKp;
                double pwrR = errorR * r.climb.cals.armHoldKp;

                double maxArmPower = r.climb.cals.releaseArmsPower;
                //all flipped because maxpower is negative
                if(pwrL < maxArmPower) pwrL = maxArmPower;
                else if(pwrL > 0) pwrL = -maxArmPower;
                if(pwrR < maxArmPower) pwrR = maxArmPower;
                else if(pwrR > 0) pwrR = -maxArmPower;

                r.climb.driveArms(pwrL, pwrR);
            } else {
                r.climb.driveArms(0.1);
            }
        } else {
            System.out.println("Stage " + stage + " skipped");
        }
    }

    @Override
    public void end(boolean interrupted){
        r.climb.driveArms(0);
        r.climb.driveWinch(0);
        //r.climb.climbArmL.setBrake(true);
        //r.climb.climbArmR.setBrake(true);
        if(!interrupted && currentStage == stage){
            sv.set(stage + 1);
        }
        System.out.println("Stage " + stage + " ended");
    }

    @Override
    public boolean isFinished(){
        boolean correctPosition = r.climb.climbWinch.getPosition() > r.climb.cals.closeToWinchedPos;
        boolean stoppedMoving = Math.abs(getPosition(idx) - getPosition(idx - r.climb.cals.prevIdxWinch)) > r.climb.cals.minRotDiffWinch;
        boolean startTimePassed = posCheckDelayStart + r.climb.cals.posCheckDelayWinch > Timer.getFPGATimestamp();
        return currentStage > stage || (startTimePassed && stoppedMoving && correctPosition && r.inputs.gather.get());
        //return currentStage > stage || !r.climb.limSwitch.get();
    }

    @Override
    public boolean isError(){
        return startTime + r.climb.cals.maxRunTimeWinch < Timer.getFPGATimestamp();
    }
}
