package frc.robot.Climber.ClimbSteps;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Climber.CmdClimb.SharedVariables;

public class Release extends CommandBase{

    RobotContainer r;

    SharedVariables sv;
    int currentStage;
    int stage;

    public Release(RobotContainer r, SharedVariables sv, int stage){
        this.r = r;
        this.sv = sv;
        this.stage = stage;
    }

    @Override
    public void initialize(){
        currentStage = sv.get();
    }

    @Override
    public boolean isFinished(){
        return currentStage > stage;
    }
}
