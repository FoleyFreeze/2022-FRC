package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdCannonAngleReset extends CommandBase{

    RobotContainer r;

    public CmdCannonAngleReset(RobotContainer r){
        this.r = r;
    }

    @Override
    public void initialize(){
        r.cannon.cals.angOffset = (r.cannon.angleMotor.getPosition() * 360) - r.cannon.cals.resetAngle;
    }

    @Override
    public void execute(){
        
    }

    @Override
    public void end(boolean interrupted){
        
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
