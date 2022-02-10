package frc.robot.Intake;

import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsIntake {
    
    public final boolean DISABLED = true;

    public CalsMotor leftMotor = new CalsMotor(MotorType.SPARK, 0);
    public CalsMotor rightMotor = new CalsMotor(MotorType.SPARK, 0);

    public CalsIntake(){

    }
}
