package frc.robot.Cannon;

import frc.robot.Robot;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsCannon {
    
    public final boolean DISABLED = true && Robot.isReal();

    double MAX_PWR = 0.5;

    double kP = 0.2;
    double kI = 0.005;
    double kD = 0.3;
    double kF = 0.0;
    double iLim = 3;

    public CalsMotor cwMotor = new CalsMotor(MotorType.SPARK, 2);
    public CalsMotor ccwMotor = new CalsMotor(MotorType.SPARK, 17);
    public CalsMotor angleMotor = new CalsMotor(MotorType.SPARK, 15).setEncUnits(60).setPIDF(kP, kI, kD, kF).setkIlim(iLim).setPIDPwrLim(MAX_PWR).brake();
    public CalsMotor leftFireMotor = new CalsMotor(MotorType.TALON, 7);
    public CalsMotor rightFireMotor = new CalsMotor(MotorType.TALON, 12);
    public CalsMotor transpMotor = new CalsMotor(MotorType.SPARK, 5);
    
    public double[] distances = {0, 0, 0, 0};
    public double[] angles = {0, 0, 0, 0};
    public double[] speeds = {0, 0, 0, 0};

    public double shootMaxAngle = 135;
    public double shootMinAngle = 45;

    public final double LAYUP_SHOOT_SPEED = 0;
    public final double LAYUP_SHOOT_ANG = 0;
    public final double LAUNCH_PAD_SHOOT_SPEED = 0;
    public final double LAUNCH_PAD_SHOOT_ANG = 0;
    public double jogInitSpeed = 0;
    public double jogSpeedInterval = 0;
    public double jogInitAng = 0;
    public double jogAngInterval = 0;
    public double minShootSpeedError = -100;
    public double maxShootSpeedError = 100;

    public double wheelOfFirePower = 1;
    public double shootTime = 0.25;

    public double preLoadTime = 0.5;
    public double loadTime = 5;
    public double tranSpeed = 0.5;
    public boolean useTimerStop = true;//using a time-based transporter as opposed to detecting current

    public double angOffset;
    public double resetAngle = 90;

    public Vector targetLocation = Vector.fromXY(0, 0);
    public double kR = 0;

    public CalsCannon(){

    }
}
