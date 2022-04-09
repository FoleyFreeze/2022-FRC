package frc.robot.Cannon;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdFire extends CommandBase{

    RobotContainer r;
    
    boolean canEndEarly;
    public boolean endEarly;

    boolean twoBalls;
    double startTime = Double.POSITIVE_INFINITY;

    double betweenWait;

    public CmdFire(RobotContainer r){
        this.r = r;
        addRequirements(r.cannon);
    }

    @Override
    public void initialize() {
        System.out.println("Cmd Fire Init");
        startTime = Timer.getFPGATimestamp();
        double angleDiff = Math.abs(r.cannon.getShooterAngle() - r.cannon.cals.resetAngle);
        //only shoot 2 in a row if we are aligned to do it
        //force 2 shots in auton since we either have 2 or we dont care about the time
        twoBalls = DriverStation.isAutonomous() || r.sensors.ballSensorLower.get() && angleDiff < r.cannon.cals.max2shootAngle.get(); 
        
        //we can end early when we are immeditely shooting 2 or there is no second one
        canEndEarly = twoBalls || !r.sensors.ballSensorLower.get(); 

        //checks if layup
        boolean layup = r.inputs.driverJoy.layUpShot() && !DriverStation.isAutonomous();
        if(layup) betweenWait = r.cannon.cals.shootTimeOneToTwoLayup.get();
        else betweenWait = r.cannon.cals.shootTimeOneToTwo.get();
    }

    @Override
    public void execute(){
        //boolean loadAngleReady = angleDiff < r.cannon.cals.minShootAngDiff;
        r.cannon.prime();
        r.cannon.fire();
        if(twoBalls && Timer.getFPGATimestamp() > startTime + betweenWait){
            r.cannon.transport(1);
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
            done = Timer.getFPGATimestamp() > startTime + r.cannon.cals.shootTimeTwo + betweenWait;
        } else {
            done = Timer.getFPGATimestamp() > startTime + r.cannon.cals.shootTimeOne;
        }

        endEarly = canEndEarly && done;

        return done;
    }
}
