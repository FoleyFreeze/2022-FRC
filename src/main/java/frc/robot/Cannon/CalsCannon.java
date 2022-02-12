package frc.robot.Cannon;

import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsCannon {
    
    public final boolean DISABLED = true;

    public CalsMotor cwMotor = new CalsMotor(MotorType.SPARK, 0);
    public CalsMotor ccwMotor = new CalsMotor(MotorType.SPARK, 0);
    public CalsMotor angleMotor = new CalsMotor(MotorType.SPARK, 0);
    public CalsMotor transpMotor = new CalsMotor(MotorType.SPARK, 0);
    
    public double[] distances = {0, 0, 0, 0};
    public double[] angles = {0, 0, 0, 0};
    public double[] speeds = {0, 0, 0, 0};

    public double shootMaxAngle = 45;
    public double shootMinAngle = -45;

    public final double LAYUP_SHOOT_SPEED = 0;
    public final double LAYUP_SHOOT_ANG = 0;
    public final double LAUNCH_PAD_SHOOT_SPEED = 0;
    public final double LAUNCH_PAD_SHOOT_ANG = 0;
    public double jogInitSpeed = 0;
    public double jogSpeedInterval = 0;
    public double jogInitAng = 0;
    public double jogAngInterval = 0;

    public CalsCannon(){

    }
}
