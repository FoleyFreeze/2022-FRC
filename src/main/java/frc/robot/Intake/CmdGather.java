package frc.robot.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdGather extends CommandBase{
    
    RobotContainer r;

    public CmdGather(RobotContainer r){
        this.r = r;
        addRequirements(r.intake);
    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){
        r.intake.intake();
    }

    @Override
    public void end(boolean interrupted){
        r.intake.stop();
        r.cannon.preLoadCargo();
    }

    @Override
    public boolean isFinished(){
        return r.sensors.hasGatheredCargo();
    }
}
