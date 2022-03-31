package frc.robot.Cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdFire extends CommandBase{

    RobotContainer r;
    
    boolean canEndEarly;
    public boolean endEarly;

    boolean twoBalls;
    double startTime = Double.POSITIVE_INFINITY;

    public CmdFire(RobotContainer r){
        this.r = r;
        addRequirements(r.cannon);
    }

    @Override
    public void initialize() {
        System.out.println("Cmd Fire Init");
        startTime = Timer.getFPGATimestamp();
        double angleDiff = Math.abs(r.cannon.getShooterAngle() - r.cannon.cals.resetAngle);
        twoBalls = r.sensors.ballSensorLower.get() && angleDiff < 6; //only shoot 2 in a row if we are aligned to do it
        
        //we can end early when we are immeditely shooting 2 or there is no second one
        canEndEarly = twoBalls || !r.sensors.ballSensorLower.get(); 
    }

    @Override
    public void execute(){
        //boolean loadAngleReady = angleDiff < r.cannon.cals.minShootAngDiff;
        r.cannon.prime();
        r.cannon.fire();
        if(twoBalls && Timer.getFPGATimestamp() > startTime + r.cannon.cals.shootTimeOne){
            r.cannon.transport(0.6);
        }
    }

    @Override
    public void end(boolean interrupted){
        System.out.println("Cmd Fire End");
        r.cannon.setSpeed(0, 0);
        r.cannon.fire(0);
        r.cannon.stopTransport();
        endEarly = false;
    }

    @Override
    public boolean isFinished(){
        boolean done;
        if(twoBalls){
            done = Timer.getFPGATimestamp() > startTime + r.cannon.cals.shootTimeTwo;
        } else {
            done = Timer.getFPGATimestamp() > startTime + r.cannon.cals.shootTimeOne;
        }

        endEarly = canEndEarly && done;

        return done;
    }
}
