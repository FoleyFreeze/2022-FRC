package frc.robot.Intake;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;

public class CmdReload extends CmdAutoGather{

    RobotContainer r;

    double startTime;

    public CmdReload(RobotContainer r){
        super(r, true);
        this.r = r;
    }

    @Override
    public void initialize(){
        super.initialize();
        System.out.println("Cmd Reload Init");
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute(){
        super.execute();
        r.cannon.prime(false); //keep speed up but dont set the angle
    }

    @Override
    public void end(boolean interrupted){
        System.out.println("Cmd Reload End");
        super.end(interrupted);
    }

    @Override
    public boolean isFinished(){
        return Timer.getFPGATimestamp() > startTime + 0.5 && r.sensors.ballSensorUpper.get();
    }
}
