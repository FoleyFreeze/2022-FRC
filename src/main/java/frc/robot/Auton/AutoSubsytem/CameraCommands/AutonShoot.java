package frc.robot.Auton.AutoSubsytem.CameraCommands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
import frc.robot.Cannon.CmdFire;
import frc.robot.Cannon.CmdPrime;
import frc.robot.Cannon.CmdReFire;
import frc.robot.Intake.CmdReload;

public class AutonShoot extends SequentialCommandGroup{

    RobotContainer r;

    CmdFire fireCmd;

    double dist;
    double startTime;

    public AutonShoot(RobotContainer r, double dist){
        addRequirements(r.cannon);
        addRequirements(r.drive);
        this.r = r;
        this.dist = dist;

        fireCmd = new CmdFire(r);
        addCommands(new SequentialCommandGroup(new CmdPrime(r), 
                                               fireCmd,
                                               new CmdReload(r),
                                               new CmdReFire(r)));
    }

    @Override
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
        System.out.println("Cmd Shoot Init");
        super.initialize();
        r.sensors.enableTgtLights(true);
    }

    @Override
    public void execute(){
        CalsAuton.autonDist = dist;
        super.execute();
    }

    @Override
    public void end(boolean interrupted){
        super.end(interrupted);
        r.sensors.enableTgtLights(false);
        r.cannon.setPower(0, 0); 
        r.cannon.transport(0);
        r.cannon.fire(0);
    }

    @Override
    public boolean isFinished(){
        return super.isFinished() || fireCmd.endEarly || (Timer.getFPGATimestamp() > startTime + 2 && DriverStation.isAutonomous());
    }
}
