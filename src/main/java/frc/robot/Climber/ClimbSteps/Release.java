package frc.robot.Climber.ClimbSteps;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Climber.ErrorCommand;
import frc.robot.Climber.CmdClimb.SharedVariables;

public class Release extends ErrorCommand{

    RobotContainer r;

    SharedVariables sv;
    int currentStage;
    int stage;

    double startTime;

    public Release(RobotContainer r, SharedVariables sv, int stage, SequentialCommandGroup sCG){
        super(sCG);
        this.r = r;
        this.sv = sv;
        this.stage = stage;
    }

    @Override
    public void initialize(){
        currentStage = sv.get();
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute(){
        r.climb.driveArms(r.climb.cals.releasePwr);
    }

    @Override
    public boolean isFinished(){
        return currentStage > stage || startTime + r.climb.cals.releaseTime > Timer.getFPGATimestamp();
    }
}
