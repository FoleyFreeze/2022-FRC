package frc.robot.Intake;

import frc.robot.Robot;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsIntake {
    
    @SuppressWarnings("unused")
    public final boolean DISABLED = false && Robot.isReal();

    double intakeRatio = 12;

    double p = 0.5;
    double d = 0.05;
    double pwrLim = 0.5;

    public CalsMotor intakeMotor = new CalsMotor(MotorType.SPARK, 6).invert().brake().setEncUnits(intakeRatio).setPIDPwrLim(pwrLim).setPIDF(p, 0, d, 0);
    public double intakeSpeed = 0.5;
    public double reverseSpeed = -0.5;

    public double kX = 0.05;
    public double yPower = 0.2;
    public double kR = 0.7 / Math.PI/2.0;
    public double autoBallMaxPwr = 0.4;
    public double maxAnglePIDDist = 0.0;
    public double minCargoDist;
    public double minCargoXError;
    public double extraGatherTime = 0.5;

    public double intakeTimeOffset = 0.1;
    public double lowFallingTime = 0.3;
    public double lowFallingKickerTimeOffset = 0.2;

    public CalsIntake(){

    }
}
