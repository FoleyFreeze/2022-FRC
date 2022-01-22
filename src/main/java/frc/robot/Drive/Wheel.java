package frc.robot.Drive;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Drive.CalsDrive.CalsWheel;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.Motor;

public class Wheel implements AutoCloseable {
    
    public CalsWheel cals;

    Motor drive; //wheel power motor
    public Motor swerve; //wheel angle motor
    AnalogInput angleEncoder;

    public Vector wheelLocation;
    public Vector driveVec;
    double encOffset;

    public Wheel(CalsWheel c){
        cals = c;
        
        drive = Motor.create(c.driveMotor);
        swerve = Motor.create(c.swerveMotor);
        angleEncoder = new AnalogInput(c.angleEncoderChannel);
        wheelLocation = c.wheelLocation;
    }

    //reset the relative encoder to match the absolute encoder
    public void resetToAbsEnc(){
        double relEncAngle = Angle.normDeg(swerve.getPosition() * 360);
        encOffset = Angle.normDeg(relEncAngle - getAbsoluteEncAngle());
        SmartDashboard.putNumber(cals.name + " encOffset", encOffset);
    }

    public double getAbsoluteEncAngle(){
        //return absolue encoder angle position
        return Angle.normDeg((angleEncoder.getVoltage() - cals.angleEncoderOffset) / 5.0 * 360);
    }

    public double calcRotAngle(Vector centerOfRot){
        Vector v = Vector.subVectors(wheelLocation, centerOfRot);

        return v.theta + (Math.PI / 2);
    }

    public void drive(boolean allZero){
        //figure out the right encoder position to target
        double targetAngle = Math.toDegrees(driveVec.theta) + encOffset;

        double currPosition = swerve.getPosition() * 360;//convert from revolutions to degrees
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
