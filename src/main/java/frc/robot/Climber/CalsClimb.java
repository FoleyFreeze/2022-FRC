package frc.robot.Climber;

import frc.robot.Robot;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;;

public class CalsClimb {

    public boolean DISABLED = false && Robot.isReal();

    public CalsMotor climbArmL = new CalsMotor(MotorType.SPARK, 13).invert().setRamp(0.1).setEncUnits(45.0);
    public CalsMotor climbArmR = new CalsMotor(MotorType.SPARK, 14).setRamp(0.1).setEncUnits(45.0);
    public CalsMotor climbWinch = new CalsMotor(MotorType.TALON, 3).setRamp(0.1).setEncUnits(64.0/36.0);

    //arms command cals
    public double posCheckDelayArms = 0.25;
    public double minRotDiffArms = 5;//degrees
    public double maxRunTimeArms = 10;
    public int prevIdxArms = 5;

    public double armPower = 0.15;
    public double releaseArmsPower = -0.1;

    //winch command cals
    public double posCheckDelayWinch = 0.1;
    public double minRotDiffWinch = 0.5;//rotations
    public double maxRunTimeWinch = 10;
    public int prevIdxWinch = 5;

    public double winchPower = 0.25;
    public double releaseWinchPower = -0.2;

    //release command cals
    public double releaseTime = 1;
    public double releasePwr = 0.3;

    public CalsClimb(){

    }
}
