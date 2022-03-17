package frc.robot.Intake;

import frc.robot.RobotContainer;

public class CmdReload extends CmdAutoGather{

    RobotContainer r;

    public CmdReload(RobotContainer r){
        super(r, false);
        this.r = r;
    }

    @Override
    public void execute(){
        super.execute();
        r.cannon.prime(false); //keep speed up but dont set the angle
    }

    @Override
    public boolean isFinished(){
        //TODO: add a time limit here too
        return r.sensors.ballSensorUpper.get();
    }
}
