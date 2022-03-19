package frc.robot.Auton.AutoSubsytem.CameraCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
import frc.robot.Cannon.CmdFire;
import frc.robot.Cannon.CmdPrime;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;

public class AutonShoot extends SequentialCommandGroup{

    RobotContainer r;
    DoubleSupplier ds = new DoubleSupplier(){
        public double getAsDouble(){
            return 0;
        }
    };

    public AutonShoot(RobotContainer r){
        this.r = r;
        addRequirements(r.cannon);
        addRequirements(r.drive);

        addCommands(new SequentialCommandGroup(new CmdPrime(r, ds), new CmdFire(r)));
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

        double zR = getTurnPwr(r.sensors.botLoc.theta);
        Vector xy;

            if(r.sensors.target.location.r < CalsAuton.minShootDist){
                xy = new Vector(0, 0);
            } else {
                xy = new Vector(0, -0.3);
            }

        //r.sensors.pdh.setSwitchableChannel(true);
        r.drive.driveSwerve(xy, zR);
    }
}
