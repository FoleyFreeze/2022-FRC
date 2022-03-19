package frc.robot.Auton.AutoSubsytem.ManualCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class ManAutonGather extends CommandBase{

    RobotContainer r;

    public ManAutonGather(RobotContainer r){
        this.r = r;
    }

    @Override
    public void execute(){
        r.intake.intake();
    }

    @Override
    public void end(boolean interrupted){
        r.intake.stop();
    }
}
