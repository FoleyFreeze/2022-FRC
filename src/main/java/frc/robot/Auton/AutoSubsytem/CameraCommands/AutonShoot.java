package frc.robot.Auton.AutoSubsytem.CameraCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
import frc.robot.Cannon.CmdFire;
import frc.robot.Cannon.CmdPrime;
import frc.robot.Cannon.CmdReFire;
import frc.robot.Intake.CmdReload;
import frc.robot.Util.Vector;

public class AutonShoot extends SequentialCommandGroup{

    RobotContainer r;
    DoubleSupplier ds = new DoubleSupplier(){
        public double getAsDouble(){
            return 0;
        }
    };

    CmdFire fireCmd;

    public AutonShoot(RobotContainer r){
        addRequirements(r.cannon);
        addRequirements(r.drive);
        this.r = r;

        fireCmd = new CmdFire(r);
        addCommands(new SequentialCommandGroup(new CmdPrime(r, ds), 
                                               fireCmd,
                                               new CmdReload(r),
                                               new CmdReFire(r)));
    }

    @Override
    public void initialize(){
        System.out.println("Cmd Shoot Init");
        super.initialize();
        r.sensors.enableTgtLights(true);
    }

    @Override
    public void execute(){
        super.execute();

        Vector xy;

        if(r.sensors.target.location.r < CalsAuton.minShootDist){
            xy = new Vector(0, 0);
        } else {
            xy = new Vector(0, -0.3);
        }

        r.drive.driveSwerveAng(xy, r.sensors.botLoc.theta, CalsAuton.autoShootTurnMaxPwr, CalsAuton.autoShootAngleKP, CalsAuton.autoShootAngleKD);
    }

    @Override
    public void end(boolean interrupted){
        super.end(interrupted);
        r.sensors.enableTgtLights(false);
        System.out.println("Cmd Shoot End");
        r.cannon.setPower(0, 0); 
    }

    @Override
    public boolean isFinished(){
        return super.isFinished() || fireCmd.endEarly;
    }
}
