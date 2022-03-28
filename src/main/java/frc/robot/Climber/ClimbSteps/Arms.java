package frc.robot.Climber.ClimbSteps;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Climber.ErrorCommand;
import frc.robot.Climber.CmdClimb.SharedVariables;

public class Arms extends ErrorCommand{

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

    public Arms(RobotContainer r, SharedVariables sv, int stage, SequentialCommandGroup sCG){
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
    }

    @Override
    public void execute(){
        super.execute();
        updatePositionArray(r.climb.climbArmL.getPosition() * 360);

        if(currentStage <= stage){
            r.climb.driveArms();
            //r.climb.releaseWinch();
            r.climb.climbWinch.setBrake(false);
        }
    }

    @Override
    public void end(boolean interrupted){
        r.climb.driveArms(0);
        r.climb.driveWinch(0);
        r.climb.climbWinch.setBrake(true);
        if(!interrupted){
            sv.set(stage + 1);
        }
        System.out.println("Stage " + stage + " ended");
    }

    @Override
    public boolean isFinished(){
        boolean stoppedMoving = Math.abs(getPosition(idx) - getPosition(idx - r.climb.cals.prevIdxArms)) > r.climb.cals.minRotDiffArms;
        boolean startTimePassed = posCheckDelayStart + r.climb.cals.posCheckDelayArms > Timer.getFPGATimestamp();
        return currentStage > stage || (startTimePassed && stoppedMoving);
    }

    @Override
    public boolean isError(){
        return startTime + r.climb.cals.maxRunTimeArms < Timer.getFPGATimestamp();
    }
}
