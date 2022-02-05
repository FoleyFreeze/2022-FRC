package frc.robot.Drive;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Drive.CalsDrive.CalsWheel;
import frc.robot.Sensors.Utilities.SwerveEncoder;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.Motor;

public class Wheel implements AutoCloseable {
    
    public CalsWheel cals;

    public Motor drive; //wheel power motor
    public Motor swerve; //wheel angle motor
    AnalogInput angleEncoder;

    public Vector wheelLocation;
    public Vector driveVec;
    double encOffset;
    public double rawAbsEncOffset;

    double prevPos = 0;
    double prevAng = 0;

    public Wheel(CalsWheel c){
        cals = c;
        
        drive = Motor.create(c.driveMotor);
        swerve = Motor.create(c.swerveMotor);
        angleEncoder = new AnalogInput(c.angleEncoderChannel);
        wheelLocation = c.wheelLocation;
        rawAbsEncOffset = cals.angleEncoderOffset;
    }

    //reset the relative encoder to match the absolute encoder
    public void resetToAbsEnc(){
        double relEncAngle = Angle.normDeg(swerve.getPosition() * 360);
        encOffset = Angle.normDeg(relEncAngle - getAbsoluteEncAngle());
        SmartDashboard.putNumber(cals.name + " encOffset", encOffset);
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
        double currentPos = drive.getPosition();
        double pos = currentPos - prevPos;
        prevPos = currentPos;

        double currentAng = Angle.normDeg(swerve.getPosition() * 360 - encOffset);
        double ang = (currentAng + prevAng) / 2;
        prevPos = currentAng;

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
        
        SmartDashboard.putNumber(cals.name + " wheel setpoint", rotationsSetpoint);

        if(!allZero){
            swerve.setPosition(rotationsSetpoint);
        }
        
        drive.setPower(magnitude * cals.maxPower);
    }

    @Override
    public void close() throws Exception {
        drive.close();
        swerve.close();
        angleEncoder.close();
    }
}
