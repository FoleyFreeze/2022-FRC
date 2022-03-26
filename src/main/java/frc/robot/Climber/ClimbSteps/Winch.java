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

    double[] winchPositionList = new double[20];
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
        if(idx > winchPositionList.length){
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
    }

    @Override
    public void execute(){
        super.execute();
        updatePositionArray(r.climb.climbWinch.getPosition() * 360);

        if(posCheckDelayStart + r.climb.cals.posCheckDelayWinch > Timer.getFPGATimestamp()){//initial delay before checking position diff
            r.climb.driveWinch();
        } else if(Math.abs(getPosition(idx) - getPosition(idx - 10)) > r.climb.cals.minRotDiffWinch){//when we have travelled enough distance to be acceptable
            r.climb.driveWinch();
        } else {//winch has stopped
            attached = true;//TODO: add extra end condition based on navX tilt
            if(extraTimeStartSet){
                extraTimeStartSet = true;
                extraTimeStart = Timer.getFPGATimestamp();
            }
        }
    }

    @Override
    public void end(boolean interrupted){
        r.climb.driveWinch(0);
        sv.set(currentStage + 1);
    }

    @Override
    public boolean isFinished(){
        return currentStage > stage || (attached && extraTimeStart + r.climb.cals.extraTimeWinch < Timer.getFPGATimestamp());
    }

    @Override
    public boolean isError(){
        return startTime + r.climb.cals.maxRunTimeWinch < Timer.getFPGATimestamp();
    }
}
