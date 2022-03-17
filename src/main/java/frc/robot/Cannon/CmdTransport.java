package frc.robot.Cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdTransport extends CommandBase{

    RobotContainer r;

    private double startTime;

    public CmdTransport(RobotContainer  r){
        this.r = r;
        addRequirements(r.cannon);
    }

    @Override 
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute(){
        r.cannon.setAngle(r.cannon.cals.resetAngle);
        r.cannon.setPower(-0.1, -0.1);
        r.cannon.transport();
    }

    @Override
    public void end(boolean interrupted){
        r.cannon.stopTransport();
        r.cannon.fire(0);
        r.cannon.setPower(0, 0);
    }

    @Override
    public boolean isFinished(){
        if(r.cannon.cals.useTimerStop){
            return startTime + r.cannon.cals.loadTime < Timer.getFPGATimestamp();
        } else {
            return true;
        }
    }
}
