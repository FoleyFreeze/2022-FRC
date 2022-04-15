package frc.robot.Intake;

import frc.robot.Robot;
import frc.robot.Util.EditableCal;
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

    public EditableCal kX = new EditableCal("gather kX", 0.03);
    public EditableCal kY = new EditableCal("gather kY", 0.015, false);
    public EditableCal yPower = new EditableCal("gather yPwr", 0.4, false);
    public EditableCal kR = new EditableCal("gather kR", 0.0, false);
    public EditableCal autoBallMaxPwr = new EditableCal("autoballMaxPwr", 0.4, false);
    public EditableCal maxAnglePIDDist = new EditableCal("maxAnglePIDDist", 5, false);
    public EditableCal minCargoDist = new EditableCal("minCargoTimeDriveDist", 20);
    public double minCargoXError = 1;
    public EditableCal extraGatherTime = new EditableCal("extra gather time", 0.9, false);
    public EditableCal gatherYOffset = new EditableCal("gather y offset", 27, false);

    public double intakeTimeOffset = 0.0;
    public double lowFallingTime = 0.3;
    public double lowFallingKickerTimeOffset = 0.2;

    public double reloadTime = 0.3;

    public CalsIntake(){

    }
}
