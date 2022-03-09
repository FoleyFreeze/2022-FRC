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
        timer = Timer.getFPGATimestamp();
    }

    @Override
    public void execute(){

        if(!r.inputs.autoGather.get() || r.sensors.ballSensorUpper.get() || r.sensors.ballSensorLower.get()){
            r.cannon.prime();
        }
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
        return r.cannon.upToSpeed() && r.cannon.angleAligned() && Timer.getFPGATimestamp() > timer + r.cannon.getPrimeTime() && r.sensors.ballSensorUpper.get();
    }
}
