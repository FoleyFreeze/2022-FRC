package frc.robot.Intake;

import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsIntake {
    
    public final boolean DISABLED = true;

    public CalsMotor intakeMotor = new CalsMotor(MotorType.SPARK, 0);
    public double intakeSpeed = 0.5;
    public double reverseSpeed = -0.5;

    public double kX;
    public double yPower;
    public double kR;

    public CalsIntake(){

    }
}
