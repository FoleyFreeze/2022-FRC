package frc.robot.Cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Util.Vector;

public class CmdPrime extends CommandBase{

    RobotContainer r;

    double timer;
    public CmdPrime(RobotContainer r){
        this.r = r;
        addRequirements(r.cannon);
    }

    @Override
    public void initialize(){
        System.out.println("Cmd Prime Init");
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
        System.out.println("Cmd Prime End");
        if(interrupted){
            //only stop shooting if we exit early   
            r.cannon.setSpeed(0,0);
        }
    }

    double alignstarttime;
    @Override
    public boolean isFinished(){
        boolean isLayup = r.inputs.operatorJoy.layUpShot() || !r.inputs.operatorJoy.hubSwitch();
        Vector xy = Vector.fromXY(r.inputs.getDriveX(), r.inputs.getDriveY());
        if(Math.abs(r.drive.angerror) > 3 || xy.r > 0.1 || !r.sensors.hasTargetImage()) alignstarttime = Timer.getFPGATimestamp();
        boolean waitForBotAlign = !r.inputs.cameraDrive() || isLayup || Timer.getFPGATimestamp() > alignstarttime + r.cannon.cals.alignTime;
        
        //System.out.format("%.1f %.1f %b\n", Timer.getFPGATimestamp()-alignstarttime, r.drive.angerror, r.cannon.upToSpeed());
        return waitForBotAlign && r.cannon.upToSpeed() /*&& r.cannon.angleAligned()*/ && Timer.getFPGATimestamp() > timer + r.cannon.getPrimeTime() /*&& r.sensors.ballSensorUpper.get()*/;
    }
}
