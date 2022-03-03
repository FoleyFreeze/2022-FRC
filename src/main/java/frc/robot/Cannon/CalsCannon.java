package frc.robot.Cannon;

import frc.robot.Robot;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsCannon {
    
    public final boolean DISABLED = false && Robot.isReal();

    double maxAnglePwr = 0.25;
    double maxShootPower = 0.9;

    double shoot_kP = 0.1;
    double shoot_kI = 0.000;
    double shoot_kD = 0.0;
    double shoot_kF = 0.0507;

    double angle_kP = 0.05;
    double angle_kI = 0.000;
    double angle_kD = 0.05;
    double angle_kF = 0.0;
    double angle_iLim = 0;

    double angleGearRatio = 60 * 59 / 24;

    public CalsMotor cwMotor = new CalsMotor(MotorType.TALON, 2).invert().setPIDF(shoot_kP, shoot_kI, shoot_kD, shoot_kF).setPIDPwrLim(0,maxShootPower);
    public CalsMotor ccwMotor = new CalsMotor(MotorType.TALON, 17).invert().setPIDF(shoot_kP, shoot_kI, shoot_kD, shoot_kF).setPIDPwrLim(0,maxShootPower);
    public CalsMotor angleMotor = new CalsMotor(MotorType.SPARK, 15).setEncUnits(angleGearRatio).setPIDF(angle_kP, angle_kI, angle_kD, angle_kF).setkIlim(angle_iLim).setPIDPwrLim(maxAnglePwr).brake();
    public CalsMotor leftFireMotor = new CalsMotor(MotorType.SPARK, 7).invert();
    public CalsMotor rightFireMotor = new CalsMotor(MotorType.SPARK, 12).invert();
    public CalsMotor transpMotor = new CalsMotor(MotorType.SPARK, 5).invert();
    
    public double[] distances = {0, 0, 0, 0};
    public double[] angles = {0, 0, 0, 0};
    public double[] speeds = {0, 0, 0, 0};

    public double shootMaxAngle = 120;
    public double shootMinAngle = 50;

    public boolean useVariableShootSpeed = true;
    public double maxVariableShootSpeed = 4500;
    public double minVariableShootSpeed = 1500;
    public final double LAYUP_SHOOT_SPEED = 2000;
    public final double LAYUP_SHOOT_ANG = 0;
    public final double LAUNCH_PAD_SHOOT_SPEED = 0;
    public final double LAUNCH_PAD_SHOOT_ANG = 0;
    public double jogInitSpeed = 0;
    public double jogSpeedInterval = 0;
    public double jogInitAng = 0;
    public double jogAngInterval = 0;
    public double minShootSpeedError = -60;
    public double maxShootSpeedError = 60;

    public double wheelOfFirePower = 1;
    public double shootTime = 0.25;
    public double minPrimeTime = 1;

    public double preLoadTime = 0.3;
    public double loadTime = 0.4;
    public double tranSpeed = 0.4;
    public boolean useTimerStop = true;//using a time-based transporter as opposed to detecting current

    public double angOffset;
    public double resetAngle = 60;

    public Vector targetLocation = Vector.fromXY(0, 0);
    public double kR = 0;

    public CalsCannon(){

    }
}
