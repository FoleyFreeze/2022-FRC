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
        //r.cannon.cals.angOffset = (r.cannon.angleMotor.getPosition() * 360) - r.cannon.cals.resetAngle;
        r.cannon.angleMotor.setEncoderPosition(r.cannon.cals.resetAngle / 360.0);
    }

    @Override
    public void execute(){
        
    }

    @Override
    public void end(boolean interrupted){
        
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
