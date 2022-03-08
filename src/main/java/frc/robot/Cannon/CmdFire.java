package frc.robot.Cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

public class CmdFire extends CommandBase{

    RobotContainer r;
    double startTime;
    boolean twoBalls;

    public CmdFire(RobotContainer r){
        this.r = r;
        addRequirements(r.cannon);
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
        twoBalls = r.sensors.ballSensorLower.get();
    }

    @Override
    public void execute(){
        r.cannon.prime();
        r.cannon.fire();
        if(twoBalls && Timer.getFPGATimestamp() > startTime + r.cannon.cals.shootTimeOne){
            r.cannon.transport();
        }
    }

    @Override
    public void end(boolean interrupted){
        super.end(interrupted);

        r.cannon.setSpeed(0, 0);
        r.cannon.fire(0);
        r.cannon.stopTransport();
    }

    @Override
    public boolean isFinished(){
        if(twoBalls){
            return Timer.getFPGATimestamp() > startTime + r.cannon.cals.shootTimeOne;
        } else {
            return Timer.getFPGATimestamp() > startTime + r.cannon.cals.shootTimeTwo;
        }
    }
}
