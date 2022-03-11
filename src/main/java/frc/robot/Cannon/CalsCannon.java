package frc.robot.Cannon;

import frc.robot.Robot;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsCannon {
    
    public final boolean DISABLED = false && Robot.isReal();

    double maxAnglePwr = 0.25;
    double maxShootPower = 0.9;

    double shoot_kP = 0.25;
    double shoot_kI = 0.0001;
    double shoot_kD = 0.0;
    double shoot_kF = 0.0507;
    double shoot_izone = 300;

    double angle_kP = 0.05;
    double angle_kI = 0.000;
    double angle_kD = 0.05;
    double angle_kF = 0.0;
    double angle_iLim = 0;
    double maxAngleSetTime = 0.25; //the amount of time the shooter is allowed to move after you release the command

    double angleGearRatio = 60 * 59 / 24;

    public CalsMotor cwMotor = new CalsMotor(MotorType.TALON, 2).invert().setPIDF(shoot_kP, shoot_kI, shoot_kD, shoot_kF).setPIDPwrLim(0,maxShootPower).setIzone(shoot_izone);
    public CalsMotor ccwMotor = new CalsMotor(MotorType.TALON, 17).invert().setPIDF(shoot_kP, shoot_kI, shoot_kD, shoot_kF).setPIDPwrLim(0,maxShootPower).setIzone(shoot_izone);
    public CalsMotor angleMotor = new CalsMotor(MotorType.SPARK, 15).setEncUnits(angleGearRatio).setPIDF(angle_kP, angle_kI, angle_kD, angle_kF).setkIlim(angle_iLim).setPIDPwrLim(maxAnglePwr).brake();
    public CalsMotor leftFireMotor = new CalsMotor(MotorType.SPARK, 7).invert().brake();
    public CalsMotor rightFireMotor = new CalsMotor(MotorType.SPARK, 12).invert().brake();
    public CalsMotor transpMotor = new CalsMotor(MotorType.SPARK, 5).invert().brake();
    
    public double[] distances = {0, 0, 0, 0};
    public double[] angles = {0, 0, 0, 0};
    public double[] speeds = {0, 0, 0, 0};

    public double shootMaxAngle = 120;
    public double shootMinAngle = 55;

    public boolean useVariableShootSpeed = false;
    public double maxVariableShootSpeed = 3800;
    public double minVariableShootSpeed = 1000;
    public final double LAYUP_SHOOT_SPEED = 2000;
    public final double LAYUP_SHOOT_ANG = 0;
    public final double LOW_SHOOT_SPEED = 0;
    public final double LOW_SHOOT_ANG = 0;
    public final double LAUNCH_PAD_SHOOT_SPEED = 0;
    public final double LAUNCH_PAD_SHOOT_ANG = 0;
    public final double TARMAC_SHOOT_SPEED = 1400;
    public final double TARMAC_SHOOT_ANG = 70;
    public double jogInitSpeed = 0;
    public double jogSpeedInterval = 25;//rpm
    public double jogInitAng = 0;
    public double jogAngInterval = 2;//degrees

    public double minShootSpeedError = -60;
    public double maxShootSpeedError = 60;
    public double minShootAngleError = -2.5;
    public double maxShootAngleError = 2.5;

    public double wheelOfFirePower = 1;
    public double shootTimeOne = 0.25;
    public double shootTimeTwo = 1;
    public double minPrimeTime15h = 0.5;
    public double minPrimeTimeSpd = 1500;
    public double minPrimeTime21h = 1.1;
    public double maxPrimeTimeSpd = 2100;

    public double preLoadTime = 1.0; //0.3
    public double preLoadPower = 0.4;
    public double loadTime = 0.4;
    public double tranPwr = 0.4;
    public boolean useTimerStop = true;//using a time-based transporter as opposed to detecting current

    public double angOffset;
    public double resetAngle = 65;

    public double sensorResetAngle = 50;
    public double sensorResetPwr = 0.07;
    public double sensorResetTime = 5;

    public Vector targetLocation = Vector.fromXY(0, 0);
    public double kR = 0;

    public CalsCannon(){

    }
}
