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
        updatePositionArray(r.climb.climbWinch.getPosition());

        if(currentStage > stage){
            r.climb.driveWinch();
            r.climb.releaseArms();
        }
    }

    @Override
    public void end(boolean interrupted){
        r.climb.driveArms(0);
        r.climb.driveWinch(0);
        if(!interrupted){
            sv.set(stage + 1);
        }
    }

    @Override
    public boolean isFinished(){
        boolean stoppedMoving = Math.abs(getPosition(idx) - getPosition(idx - r.climb.cals.prevIdxWinch)) > r.climb.cals.minRotDiffWinch;
        boolean startTimePassed = posCheckDelayStart + r.climb.cals.posCheckDelayWinch > Timer.getFPGATimestamp();
        return currentStage > stage || (startTimePassed && stoppedMoving);
    }

    @Override
    public boolean isError(){
        return startTime + r.climb.cals.maxRunTimeWinch < Timer.getFPGATimestamp();
    }
}
