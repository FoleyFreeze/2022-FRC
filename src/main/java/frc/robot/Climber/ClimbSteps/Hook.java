package frc.robot.Climber.ClimbSteps;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Climber.ErrorCommand;
import frc.robot.Climber.CmdClimb.SharedVariables;

public class Hook extends ErrorCommand {
    RobotContainer r;
    SharedVariables sv;
    int stage;
    int currentStage;

    double winchInitPos;

    double startTime;
    boolean fallenTooFar;

    public Hook(RobotContainer r, SharedVariables sv, int stage, SequentialCommandGroup sCG){
        super(sCG);
        this.r = r;
        this.sv = sv;
        this.stage = stage;
    }

    @Override
    public void initialize(){
        currentStage = sv.get();

        winchInitPos = r.climb.climbWinch.getPosition();

        startTime = Timer.getFPGATimestamp();
        System.out.println("Stage " + stage + " init");
        System.out.println(currentStage + " " + stage);
    }

    @Override
    public void execute(){

        //drive the arms to 10 degrees
        double armL = r.climb.climbArmL.getPosition() * 360;
        double armR = r.climb.climbArmR.getPosition() * 360;
        double errorL = r.climb.cals.armStartPoint - armL;
        double errorR = r.climb.cals.armStartPoint - armR;
        double pwrL = errorL * r.climb.cals.armHoldKp;
        double pwrR = errorR * r.climb.cals.armHoldKp;

        double maxArmPower = r.climb.cals.releaseArmsPower;
        //all flipped because maxpower is negative
        if(pwrL < maxArmPower) pwrL = maxArmPower;
        else if(pwrL > -maxArmPower) pwrL = -maxArmPower;
        if(pwrR < maxArmPower) pwrR = maxArmPower;
        else if(pwrR > -maxArmPower) pwrR = -maxArmPower;
        r.climb.driveArms(pwrL, pwrR);


        fallenTooFar = Math.abs(r.climb.climbWinch.getPosition() - winchInitPos) > r.climb.cals.allowedFallDist;
        if(fallenTooFar){
            System.out.println("Fell: " + (r.climb.climbWinch.getPosition() - winchInitPos) + " revs");
        }
        
        super.execute();
        if(currentStage <= stage){
            if(fallenTooFar){
                r.climb.climbWinch.setBrake(true);
            } else {
                r.climb.climbWinch.setBrake(false);
            }
        } else {
            System.out.println("Stage " + stage + " skipped");
        }
    }

    @Override
    public void end(boolean interrupted){
        r.climb.driveArms(0);
        r.climb.driveWinch(0);
        r.climb.climbWinch.setBrake(true);
        
        /*
        if(fallenTooFar){
            //if we fell, go back to the winch stage
            sv.set(stage-1);
        } else if(!interrupted && currentStage == stage){
        */    sv.set(stage + 1);
        //}
        System.out.println("Stage " + stage + " ended");
    }

    @Override
    public boolean isFinished(){
        boolean time = startTime + r.climb.cals.maxHookTime < Timer.getFPGATimestamp();
        return currentStage > stage || r.inputs.leftTriggerRisingEdge || (time/* && !fallenTooFar*/);
    }

    @Override
    public boolean isError(){
        return false;// fallenTooFar;
    }


}
