package frc.robot.Cannon;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Vector;

public class CmdShoot extends SequentialCommandGroup{

    RobotContainer r;
    double angerror;
    DoubleSupplier ds = new DoubleSupplier() {
        public double getAsDouble(){
            return angerror;
        }
    };

    public CmdShoot(RobotContainer r){
        addRequirements(r.cannon);
        addRequirements(r.drive);
        this.r = r;

        addCommands(new SequentialCommandGroup(new CmdPrime(r, ds), new CmdFire(r)));
    }

    @Override
    public void execute(){
        super.execute();

        double x = r.inputs.getDriveX();
        double y = r.inputs.getDriveY();
        double zR;

        Vector xy = Vector.fromXY(x, y);
        
        if(r.inputs.cameraDrive() && r.sensors.hasTargetImage()){
            //Vector targetPos = Vector.subVectors(r.cannon.cals.targetLocation, r.sensors.botLoc);
            Vector targetPos = r.sensors.target.location;

            SmartDashboard.putNumber("RawAngle2",Math.toDegrees(targetPos.theta));
            double angleturned = r.sensors.target.angle - r.sensors.botAng;
            SmartDashboard.putNumber("RobotTargetAngle", angleturned);
            
            angerror = Math.toDegrees(targetPos.theta) - angleturned;

            SmartDashboard.putNumber("TargetAng", angerror);
            //TODO: replace w/ call to driveSwerveAngle
            zR = r.cannon.cals.kR * angerror;//correct for shooter location
            if(zR > r.cannon.cals.maxPower){
                zR = r.cannon.cals.maxPower;
            }
        } else {
            zR = r.inputs.getDrivezR();
        }

        Inputs.mapSquareToCircle(xy);

        r.drive.driveSwerve(xy, zR);
    } 

}
