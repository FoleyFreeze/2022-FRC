package frc.robot.Climber.ClimbSteps;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Climber.ErrorCommand;
import frc.robot.Climber.CmdClimb.SharedVariables;

public class Reset extends ErrorCommand{

    RobotContainer r;

    SharedVariables sv;
    int currentStage;
    int stage;

    double startTime;

    public Reset(RobotContainer r, SharedVariables sv, int stage, SequentialCommandGroup sCG){
        super(sCG);
        this.r = r;
        this.sv = sv;
        this.stage = stage;
    }

    @Override
    public void initialize(){
        currentStage = sv.get();
        startTime = Timer.getFPGATimestamp();
        System.out.println("Stage " + stage + " init");
        System.out.println(currentStage + " " + stage);
    }

    @Override
    public void execute(){
        if(currentStage <= stage){
            r.climb.resetEncoders(false);
        } else {
            System.out.println("Stage " + stage + " skipped");
        }
    }

    @Override
    public void end(boolean interrupted){
        if(!interrupted && currentStage == stage){
            sv.set(stage + 1);
        }
        System.out.println("Stage " + stage + " ended");
    }

    @Override
    public boolean isFinished(){
        return true;
        //return currentStage > stage || startTime + r.climb.cals.releaseTime > Timer.getFPGATimestamp();
    }
}
