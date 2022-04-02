package frc.robot.Drive;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.Drive.CalsDrive.CalsWheel;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.Motor;

public class Wheel implements AutoCloseable {
    
    public CalsWheel cals;
    RobotContainer r;

    public Motor drive; //driving power motor
    public Motor swerve; //wheel angle motor
    AnalogInput angleEncoder;

    public Vector wheelLocation;
    public Vector driveVec;
    double encOffset;
    public double rawAbsEncOffset;

    double prevPos = 0;
    double prevAng = 0;

    double lastUpdateTime;
    int errorCount = 0;
    double prevSwerveSetpoint;

    public Wheel(CalsWheel c, RobotContainer r){
        cals = c;
        this.r = r;
        
        drive = Motor.create(c.driveMotor);
        drive.resetEncoder();
        swerve = Motor.create(c.swerveMotor);
        angleEncoder = new AnalogInput(c.angleEncoderChannel);
        wheelLocation = c.wheelLocation;
        rawAbsEncOffset = cals.angleEncoderOffset;
    }

    //reset the relative encoder to match the absolute encoder
    public void resetToAbsEnc(){
        double relEncAngle = Angle.normDeg(swerve.getPosition() * 360);
        encOffset = Angle.normDeg(relEncAngle - getAbsoluteEncAngle());
        //SmartDashboard.putNumber(cals.name + " encOffset", encOffset);
    }

    public double learnWheelAngle(){
        double offset = angleEncoder.getVoltage();
        rawAbsEncOffset = offset;
        resetToAbsEnc();
        return offset;
    }

    public double getAbsoluteEncAngle(){
        //return absolue encoder angle position
        return Angle.normDeg((angleEncoder.getVoltage() - rawAbsEncOffset) / 5.0 * 360);
    }

    public double calcRotAngle(Vector centerOfRot){
        Vector v = Vector.subVectors(wheelLocation, centerOfRot);

        return v.theta + (Math.PI / 2);
    }

    public Vector deltaVec(){
        double currentPos = drive.getPosition() + swerve.getPosition() * cals.driveInPerSwerveRotation;
        double pos = currentPos - prevPos;
        prevPos = currentPos;

        double currentAng = Angle.normDeg(swerve.getPosition() * 360 - encOffset);
        double ang = (currentAng + prevAng) / 2;
        prevAng = currentAng;

        return new Vector(pos, Math.toRadians(ang));
    }

    public void drive(boolean allZero){
        //figure out the right encoder position to target
        double targetAngle = Math.toDegrees(driveVec.theta) + encOffset;

        double currPosition = swerve.getPosition() * 360;
        double angleDiff = Angle.normDeg(targetAngle - currPosition);

        double magnitude = driveVec.r;
        
        //if we are going the long way
        if(Math.abs(angleDiff) > 90){
            magnitude = -magnitude;
            if(angleDiff > 0){
                angleDiff -= 180;
            } else {
                angleDiff += 180;
            }
        }

        //convert back to motor rotations
        double rotationsSetpoint = (angleDiff + currPosition) / 360;
        
        //SmartDashboard.putNumber(cals.name + " wheel setpoint", rotationsSetpoint);

        if(!allZero && rotationsSetpoint != prevSwerveSetpoint){
            swerve.setPosition(rotationsSetpoint);
            prevSwerveSetpoint = rotationsSetpoint;
            lastUpdateTime = Timer.getFPGATimestamp();
        }
        
        if(cals.useVelocityControl){
            drive.setSpeed(magnitude * cals.maxPower * cals.maxVelocity);
        } else{
            drive.setPower(magnitude * cals.maxPower);
        }
    }

    public double getDriveTemp(){
        return drive.getTemp();
    }

    public double getSwerveTemp(){
        return swerve.getTemp();
    }

    public void periodic(){
        if(Timer.getFPGATimestamp() > lastUpdateTime + 1){
            swerve.setPower(0);
            int channel = cals.swerveMotor.channel;
            if(channel == 20) channel = 0;
            if(r.sensors.pdh.getCurrent(channel) > 5){
                errorCount++;
            }
        }
    }

    @Override
    public void close() throws Exception {
        drive.close();
        swerve.close();
        angleEncoder.close();
    }
}
