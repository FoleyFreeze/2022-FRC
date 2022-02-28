package frc.robot.Intake;

import frc.robot.Robot;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsIntake {
    
    public final boolean DISABLED = true && Robot.isReal();

    public CalsMotor intakeMotor = new CalsMotor(MotorType.SPARK, 6);
    public double intakeSpeed = 0.5;
    public double reverseSpeed = -0.5;

    public double kX = 0.3;
    public double yPower = 0.3;
    public double kR = 0.1;

    public CalsIntake(){

    }
}
