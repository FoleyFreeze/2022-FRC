package frc.robot.Drive;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Drive.CalsDrive.CalsWheel;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.Motor;

public class Wheel implements AutoCloseable {
    
    CalsWheel cals;

    Motor drive; //wheel power motor
    Motor swerve; //wheel angle motor
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
        double relEncAngle = Angle.normDeg(swerve.getPosition());
        encOffset = Angle.normDeg(relEncAngle - getAbsoluteEncAngle());
    }

    public double getAbsoluteEncAngle(){
        //return absolue encoder angle position
        return Angle.normDeg((angleEncoder.getVoltage() - cals.angleEncoderOffset) / 5.0 * 360);
    }

    public double calcRotAngle(Vector centerOfRot){
        Vector v = Vector.subVectors(wheelLocation, centerOfRot);

        return v.theta + (Math.PI / 2);
    }

    public void drive(){
        //figure out the right encoder position to target
        double targetAngle = Math.toDegrees(driveVec.theta) - encOffset;

        double currPosition = swerve.getPosition();
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

        swerve.setPosition(angleDiff + currPosition);
        
        drive.setPower(magnitude);
    }

    @Override
    public void close() throws Exception {
        drive.close();
        swerve.close();
        angleEncoder.close();
    }
}
