package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
import frc.robot.Cannon.CmdPrime;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;

public class AutonShoot extends SequentialCommandGroup{

    RobotContainer r;

    public AutonShoot(RobotContainer r){
        this.r = r;
        addRequirements(r.cannon);
        addRequirements(r.drive);

        addCommands(new SequentialCommandGroup(new CmdPrime(r), new AutonFire(r)));
    }

    private double getTurnPwr(double tgtAng){
        double val = (Angle.normDeg(Math.toDegrees(tgtAng)) - r.sensors.botAng) * CalsAuton.autoShootAngleKP;
        if(val > CalsAuton.autoShootAngleMaxPwr){
            val = CalsAuton.autoShootAngleMaxPwr;
        }
        return val;
    }

    @Override
    public void execute(){
        super.execute();

        double zR = getTurnPwr(r.sensors.botLoc.theta);;
        Vector xy;

            if(r.sensors.target.location.r < CalsAuton.minShootDist){
                xy = new Vector(0, 0);
            } else {
                xy = new Vector(0, 0);
            }

        r.drive.driveSwerve(xy, zR);
    }

    @Override
    public boolean isFinished(){
        return !r.sensors.ballSensorLower.get() && !r.sensors.ballSensorUpper.get(); //we have shot all cargo
    }
}
