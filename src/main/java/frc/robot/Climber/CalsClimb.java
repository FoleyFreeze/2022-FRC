package frc.robot.Climber;

import frc.robot.Robot;
import frc.robot.Util.EditableCal;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;;

public class CalsClimb {

    public boolean DISABLED = false && Robot.isReal();

    //climb arm ratio is actually 45 * 42 / 44; but we cal'd for 45:1 so w/e
    public CalsMotor climbArmL = new CalsMotor(MotorType.SPARK, 13).invert().setRamp(0.4).setEncUnits(45.0).brake();
    public CalsMotor climbArmR = new CalsMotor(MotorType.SPARK, 14).setRamp(0.4).setEncUnits(45.0).brake();
    public CalsMotor climbWinch = new CalsMotor(MotorType.TALON, 3).invert().setRamp(0.2).setEncUnits(64.0/36.0).brake();

    //arms command cals
    public double posCheckDelayArms = 0.5;
    public double minRotDiffArms = 5;//degrees
    public double maxRunTimeArms = 10;
    public int prevIdxArms = 5;

    public double armBasePower = 0.2;
    public double armPower = 0.2;
    public double releaseArmsPower = -0.15;

    public double armStartPoint = 10;
    public double armHoldPoint = 77; //degrees
    public double armLowHoldPoint = 53;
    public double armLowPoint = 25;
    public double armHoldKp = 0.03; //2% per degree
    public double winchOutRevs = -19;
    public double winchOutRevsFar = -37;
    public double winchBreakRevs = -30;
    public double targetWinchOutRevs = -38;
    public double targetWinchOutRevsFar = -45;
    public double winchKp = 0.07; //7% per rev
    public double maxWinchPIDpwr = 0.8;
    public double closeToWinchedPos = -5;

    //winch command cals
    public double posCheckDelayWinch = 0.5;
    public double minRotDiffWinch = 0.1;//rotations
    public double maxRunTimeWinch = 10;
    public int prevIdxWinch = 5;

    public EditableCal winchPower = new EditableCal("WinchPower", 0.75);
    public double releaseWinchPower = -1.0;//FULL POWER!!!

    public double winchStallTime = 0.0;

    //hook command cals
    public double maxHookTime = 0.75;
    public double allowedFallDist = 15;

    //release command cals
    public double releaseTime = 1;
    public double releasePwr = 0.3;

    public CalsClimb(){

    }
}
