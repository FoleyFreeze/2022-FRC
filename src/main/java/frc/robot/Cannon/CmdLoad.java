package frc.robot.Cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdLoad extends CommandBase{

    RobotContainer r;
    SysCannon c;

    private double startTime;

    public CmdLoad(RobotContainer  r){
        this.r = r;
        c = r.cannon;
        addRequirements(r.cannon);
    }

    @Override 
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute(){
        c.setAngle(65);
        c.setPower(-0.1, -0.1);
        c.transport();
    }

    @Override
    public void end(boolean interrupted){
        c.stopTransport();
        c.fire(0);
        c.setPower(0, 0);
    }

    @Override
    public boolean isFinished(){
        if(c.cals.useTimerStop){
            return startTime + c.cals.loadTime < Timer.getFPGATimestamp();
        } else {
            return true;
        }
    }
}
