package frc.robot.Cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdCannonSensorReset extends CommandBase{

    RobotContainer r;

    double startTime;

    public CmdCannonSensorReset(RobotContainer r){
        this.r = r;
    }

    @Override
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute(){
        r.cannon.angleMotor.setPower(r.cannon.cals.sensorResetPwr);
    }

    @Override
    public void end(boolean interrupted){
        r.cannon.angleMotor.setPower(0);
    }

    @Override
    public boolean isFinished(){
        if(!r.sensors.cannonAngleSensor.get()){
            r.cannon.angleMotor.setEncoderPosition(r.cannon.cals.sensorResetAngle / 360);
            return true;
        } else {
            return Timer.getFPGATimestamp() > startTime + r.cannon.cals.sensorResetTime;
        }
    }
}
