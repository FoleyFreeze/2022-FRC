package frc.robot.Cannon;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Intake.CmdReload;
import frc.robot.Util.Vector;

public class CmdShoot extends SequentialCommandGroup{

    RobotContainer r;
    double angerror;
    DoubleSupplier ds = new DoubleSupplier() {
        public double getAsDouble(){
            return angerror;
        }
    };

    CmdFire fireCmd;

    public CmdShoot(RobotContainer r){
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

        double x = r.inputs.getDriveX();
        double y = r.inputs.getDriveY();
        double zR;

        Vector xy = Vector.fromXY(x, y);
        
        Inputs.mapSquareToCircle(xy);

        if(r.inputs.cameraDrive() && r.sensors.hasTargetImage()){
            Vector targetPos = r.sensors.target.location;

            SmartDashboard.putNumber("TargetAng", angerror);

            r.drive.driveSwerveAng(xy, targetPos.theta, r.cannon.cals.maxPower, r.cannon.cals.drivekR.get(), r.cannon.cals.drivekD.get());
        } else {
            zR = r.inputs.getDrivezR();
            if(zR > r.cannon.cals.maxPower) zR = r.cannon.cals.maxPower;
            r.drive.driveSwerve(xy, zR);
        }
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
