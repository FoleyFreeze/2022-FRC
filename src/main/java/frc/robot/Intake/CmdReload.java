package frc.robot.Intake;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;

public class CmdReload extends CmdAutoGather{

    RobotContainer r;
    double startTime;

    public CmdReload(RobotContainer r){
        super(r, false);
        this.r = r;
    }

    @Override
    public void execute(){
        super.execute();
        r.cannon.prime(false); //keep speed up but dont set the angle

        if (r.sensors.ballSensorUpper.risingEdge()){
            startTime = Timer.getFPGATimestamp();
        }
    }

    @Override
    public boolean isFinished(){
        return startTime + r.intake.cals.reloadTime < Timer.getFPGATimestamp();
    }
}
