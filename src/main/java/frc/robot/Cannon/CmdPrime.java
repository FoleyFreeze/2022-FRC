package frc.robot.Cannon;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Vector;

public class CmdPrime extends CommandBase{

    RobotContainer r;
    DoubleSupplier ds;

    double timer;
    public CmdPrime(RobotContainer r, DoubleSupplier ds){
        this.r = r;
        this.ds = ds;
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

    double alignstarttime;
    @Override
    public boolean isFinished(){
        if(Math.abs(ds.getAsDouble()) > 5) alignstarttime = Timer.getFPGATimestamp();
        boolean waitForBotAlign = !r.inputs.cameraDrive() || Timer.getFPGATimestamp() > alignstarttime + 0.3;
        return waitForBotAlign && r.cannon.upToSpeed() && r.cannon.angleAligned() && Timer.getFPGATimestamp() > timer + r.cannon.getPrimeTime() && r.sensors.ballSensorUpper.get();
    }
}
