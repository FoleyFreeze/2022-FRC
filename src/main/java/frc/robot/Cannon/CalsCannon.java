package frc.robot.Cannon;

import frc.robot.Util.Motor.CalsMotor;

public class CalsCannon {
    
    public final int SHOOT_MOTOR_CHANNEL = 0;
    public CalsMotor cwMotor;
    public CalsMotor ccwMotor;
    public CalsMotor angleMotor;

    public final double RPM_TO_POWER = 0;

    public double[] distances = {0, 0, 0, 0};
    public double[] angles = {0, 0, 0, 0};
    public double[] speeds = {0, 0, 0, 0};

    public final double LAYUP_SHOOT_SPEED = 0;
    public final double LAUNCH_PAD_SHOOT_SPEED = 0;

    public CalsCannon(){

    }
}
