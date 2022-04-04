package frc.robot.Cannon;

import frc.robot.Robot;
import frc.robot.Auton.CalsAuton;
import frc.robot.Util.EditableCal;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsCannon {
    
    public final boolean DISABLED = false && Robot.isReal();

    EditableCal maxAnglePwr = new EditableCal("shoot angle pwr", 0.25);
    EditableCal maxShootPower = new EditableCal("shoot pwr", 0.9);

    public EditableCal maxTgtImgs = new EditableCal("max tgt imgs", 5);

    EditableCal shoot_kP = new EditableCal("shoot kP", 0.1, false);
    EditableCal shoot_kI = new EditableCal("shoot kI", 0.002, false);
    EditableCal shoot_kD = new EditableCal("shoot kD", 0.0, false);
    EditableCal shoot_kF = new EditableCal("shoot kF", 0.0507, false);
    double shoot_izone = 300;

    EditableCal angle_kP = new EditableCal("angle kP", 0.05);
    EditableCal angle_kI = new EditableCal("angle kI", 0.000);
    EditableCal angle_kD = new EditableCal("angle kD", 0.05);
    EditableCal angle_kF = new EditableCal("angle kF", 0.0, false);
    double angle_iLim = 0;
    double maxAngleSetTime = 0.25; //the amount of time the shooter is allowed to move after you release the command

    double angleGearRatio = 60 * 59 / 24;

    public CalsMotor cwMotor = new CalsMotor(MotorType.TALON, 2).invert().setPIDF(shoot_kP, shoot_kI, shoot_kD, shoot_kF).setPIDPwrLim(EditableCal.ZERO,maxShootPower).setIzone(shoot_izone);
    public CalsMotor ccwMotor = new CalsMotor(MotorType.TALON, 17).invert().setPIDF(shoot_kP, shoot_kI, shoot_kD, shoot_kF).setPIDPwrLim(EditableCal.ZERO,maxShootPower).setIzone(shoot_izone);
    public CalsMotor angleMotor = new CalsMotor(MotorType.SPARK, 15).setEncUnits(angleGearRatio).setPIDF(angle_kP, angle_kI, angle_kD, angle_kF).setkIlim(angle_iLim).setPIDPwrLim(maxAnglePwr).brake();
    public CalsMotor leftFireMotor = new CalsMotor(MotorType.SPARK, 7).invert().brake();
    public CalsMotor rightFireMotor = new CalsMotor(MotorType.SPARK, 12).invert().brake();
    public CalsMotor transpMotor = new CalsMotor(MotorType.SPARK, 5).invert().brake();
    
    public double[] distances = {20, 50};
    public double[] angles = {70,70};
    public double[] speeds = {1375,1375};

    public double shootMaxAngle = 120;
    public double shootMinAngle = 55;

    public boolean useVariableShootSpeed = false;
    public double maxVariableShootSpeed = 3800;
    public double minVariableShootSpeed = 1000;
    public final double LAYUP_SHOOT_SPEED = 1400;
    public final double LAYUP_SHOOT_ANG = 83;
    public final double LOW_SHOOT_SPEED = 775;
    public final double LOW_SHOOT_ANG = 78;
    public final double LAUNCH_PAD_SHOOT_SPEED = 1850;
    public final double LAUNCH_PAD_SHOOT_ANG = 57;
    public final double TARMAC_SHOOT_SPEED = CalsAuton.joeShotPrimeSpeed + 50;
    public final double TARMAC_SHOOT_ANG = CalsAuton.joeShotPrimeAng + 2;
    public double jogInitSpeed = 0;
    public double jogSpeedInterval = 25;//rpm
    public double jogInitAng = 0;
    public double jogAngInterval = 2;//degrees

    public double minShootSpeedError = -60;
    public double maxShootSpeedError = 60;
    public double minShootAngleError = -2.5;
    public double maxShootAngleError = 2.5;

    public double minShootAngDiff = 6;//degrees

    public double wheelOfFirePower = 1;
    public double shootTimeOne = 0.25;
    public double shootTimeTwo = 1;
    public double minPrimeTime15h = 0.7;
    public double minPrimeTimeSpd = 1500;
    public double minPrimeTime21h = 1.1;
    public double maxPrimeTimeSpd = 2100;
    public double alignTime = 0.3;

    public double preLoadTime = 0.4; //0.3
    public double preLoadPower = 0.4;
    public double fireLoadPwr = 0.2;
    public double loadTime = 0.4;
    public double kickLoadTime = 0.25;
    public double tranPwr = 0.4;
    public boolean useTimerStop = true;//using a time-based transporter as opposed to detecting current

    public double angOffset;
    public double resetAngle = 65;

    public double climbCannonAng = 55; //110 was for the passive climb

    public double sensorResetAngle = 50;
    public double sensorResetPwr = 0.07;
    public double sensorResetTime = 5;

    public Vector targetLocation = Vector.fromXY(0, 0);
    public double maxPower = 0.2;
    public EditableCal drivekR = new EditableCal("shootDrive kR", 0.02);
    public EditableCal drivekD = new EditableCal("shootDrive kD", -0.0008);

    public boolean useAutoLoad = false;

    public CalsCannon(){

    }
}
