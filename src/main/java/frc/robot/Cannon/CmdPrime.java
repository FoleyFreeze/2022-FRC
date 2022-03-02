package frc.robot.Cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Vector;

public class CmdPrime extends CommandBase{

    RobotContainer r;

    double timer;
    public CmdPrime(RobotContainer r){
        this.r = r;
        addRequirements(r.cannon);
        addRequirements(r.drive);
    }

    @Override
    public void initialize(){
        timer = Timer.getFPGATimestamp() + r.cannon.cals.minPrimeTime;
    }

    @Override
    public void execute(){
        r.cannon.prime();

        double x;
        double y;
        double zR;

        Vector xy;
        
        if(r.inputs.cameraDrive() && r.sensors.hasTargetImage()){
            Vector targetPos = Vector.subVectors(r.cannon.cals.targetLocation, r.sensors.botLoc);
            targetPos.theta -= Math.toRadians(r.sensors.botAng);
            
            zR = r.cannon.cals.kR * targetPos.theta;
        } else {
            zR = r.inputs.getDrivezR();
        }

        x = r.inputs.getDriveX();
        y = r.inputs.getDriveY();
        xy = Vector.fromXY(x, y);
        Inputs.mapSquareToCircle(xy);
        r.drive.driveSwerve(xy, zR);
    }

    @Override
    public void end(boolean interrupted){
        if(interrupted){
            //only stop shooting if we exit early
            r.cannon.setSpeed(0,0);
        }
    }

    @Override
    public boolean isFinished(){
        return r.cannon.upToSpeed() && !r.sensors.botIsMoving() && Timer.getFPGATimestamp() > timer;
    }


}
