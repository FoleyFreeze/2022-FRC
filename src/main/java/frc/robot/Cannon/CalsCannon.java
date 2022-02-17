package frc.robot.Cannon;

import frc.robot.Robot;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsCannon {
    
    public final boolean DISABLED = true && Robot.isReal();

    public CalsMotor cwMotor = new CalsMotor(MotorType.SPARK, 0);
    public CalsMotor ccwMotor = new CalsMotor(MotorType.SPARK, 1);
    public CalsMotor angleMotor = new CalsMotor(MotorType.SPARK, 2);
    public CalsMotor leftFireMotor = new CalsMotor(MotorType.SPARK, 0);
    public CalsMotor rightFireMotor = new CalsMotor(MotorType.SPARK, 0);
    public CalsMotor transpMotor = new CalsMotor(MotorType.SPARK, 0);
    
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
    public double loadTime = -1;//TODO: put a time in here
    public double tranSpeed = 0.5;
    public boolean useTimerStop = true;//using a time-based transporter as opposed to detecting current

    public CalsCannon(){

    }
}
