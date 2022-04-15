package frc.robot.Cannon;

import frc.robot.Robot;
import frc.robot.Auton.CalsAuton;
import frc.robot.Util.EditableCal;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;

public class CalsCannon {
    
    @SuppressWarnings("unused")
    public final boolean DISABLED = false && Robot.isReal();

    EditableCal maxAnglePwr = new EditableCal("shoot angle pwr", 0.25, false);
    EditableCal maxShootPower = new EditableCal("shoot pwr", 0.9, false);

    public EditableCal maxTgtImgs = new EditableCal("max tgt imgs", 3, true);

    EditableCal shoot_kP = new EditableCal("shoot kP", 0.16, false);
    EditableCal shoot_kI = new EditableCal("shoot kI", 0.0009, false);
    EditableCal shoot_kD = new EditableCal("shoot kD", 0.8, false);
    EditableCal shoot_kF = new EditableCal("shoot kF", 0.05, false);
    double shoot_izone = 200;

    EditableCal angle_kP = new EditableCal("angle kP", 0.05, false);
    EditableCal angle_kI = new EditableCal("angle kI", 0.000, false);
    EditableCal angle_kD = new EditableCal("angle kD", 0.05, false);
    EditableCal angle_kF = new EditableCal("angle kF", 0.0, false);
    double angle_iLim = 0;
    double maxAngleSetTime = 0.5; //the amount of time the shooter is allowed to move after you release the command

    double angleGearRatio = 60 * 59 / 24;

    public CalsMotor cwMotor = new CalsMotor(MotorType.TALON, 2).invert().setPIDF(shoot_kP, shoot_kI, shoot_kD, shoot_kF).setPIDPwrLim(EditableCal.ZERO,maxShootPower).setIzone(shoot_izone);
    public CalsMotor ccwMotor = new CalsMotor(MotorType.TALON, 17).invert().setPIDF(shoot_kP, shoot_kI, shoot_kD, shoot_kF).setPIDPwrLim(EditableCal.ZERO,maxShootPower).setIzone(shoot_izone);
    public CalsMotor angleMotor = new CalsMotor(MotorType.SPARK, 15).setEncUnits(angleGearRatio).setPIDF(angle_kP, angle_kI, angle_kD, angle_kF).setkIlim(angle_iLim).setPIDPwrLim(maxAnglePwr).brake();
    public CalsMotor leftFireMotor = new CalsMotor(MotorType.SPARK, 7).invert().brake();
    public CalsMotor rightFireMotor = new CalsMotor(MotorType.SPARK, 12).invert().brake();
    public CalsMotor transpMotor = new CalsMotor(MotorType.SPARK, 5).invert().brake();
    
    public EditableCal useDistanceLookup = new EditableCal("UseImgDist", 1);
    public double[] distances = {     59,      69,      77,      86,      93,      114,      120,      127,      135,      145,      157,      175,      197,      205,      218,      228};
    public double[] angles =    {     80,      78,      76,      74,      74,       70,       70,       70,       70,       70,       70,       70,       66,       64,       64,       64};
    public double[] speeds =    {1350+25, 1350+25, 1350+25, 1400+25, 1375+75, 1375+125, 1400+125, 1425+125, 1450+150, 1500+150, 1550+200, 1650+225, 1775+225, 1800+300, 1900+300, 1900+500};

    public double shootMaxAngle = 120;
    public double shootMinAngle = 55;
    public double potMaxAngle = 122;
    public double potMinAngle = 45;
    public double voltsPerDegree = 5/270.0 * 1.1609;//0.01819;
    public double potResetDeltaAngle = 2;
    public double potMaxMovement = 10;
    public double potVoltOffset = 1.672; //set this to the analog voltage when the limit switch is pressed

    public boolean useVariableShootSpeed = false;
    public double maxVariableShootSpeed = 3800;
    public double minVariableShootSpeed = 1000;
    public final double LAYUP_SHOOT_SPEED = 1375;
    public final double LAYUP_SHOOT_ANG = 82;//back
    public final double LOW_SHOOT_SPEED = 750;
    public final double LOW_SHOOT_ANG = 68;
    public EditableCal flipAngOffset = new EditableCal("flipAngOffset", 178);
    public final double LAUNCH_PAD_SHOOT_SPEED = 1850;
    public final double LAUNCH_PAD_SHOOT_ANG = 57;
    public EditableCal TARMAC_SHOOT_SPEED = new EditableCal("ShootSpeed", CalsAuton.joeShotPrimeSpeed+50);
    public EditableCal TARMAC_SHOOT_ANG = new EditableCal("ShootAng", CalsAuton.joeShotPrimeAng);
    //public final double TARMAC_SHOOT_SPEED = CalsAuton.joeShotPrimeSpeed + 50;
    //public final double TARMAC_SHOOT_ANG = CalsAuton.joeShotPrimeAng + 2;
    public double jogInitSpeed = 0;
    public double jogSpeedInterval = 25;//rpm
    public double jogInitAng = 0;
    public double jogAngInterval = 2;//degrees

    public double minShootSpeedError = -50;
    public double maxShootSpeedError = 50;
    public double minShootAngleError = -2.5;
    public double maxShootAngleError = 2.5;

    public double minShootAngDiff = 6;//degrees

    public EditableCal wheelOfFirePower = new EditableCal("FirePowah", 0.5);
    public double shootTimeOne = 0.3;
    public EditableCal shootTimeOneToTwo = new EditableCal("shot_spacing_time", 0.1);
    public EditableCal shootTimeOneToTwoLayup = new EditableCal("layup_spacing", 0.4);
    public double shootTimeTwo = 0.75;
    public double minPrimeTime15h = 0.7-0.25;
    public double minPrimeTimeSpd = 1500;
    public double minPrimeTime21h = 1.1-0.25;
    public double maxPrimeTimeSpd = 2100;
    public double alignTime = 0.3;

    public double preLoadTime = 0.4; //0.3
    public double preLoadPower = 0.4;
    public double fireLoadPwr = 0.2;
    public double loadTime = 0.4;
    public double kickLoadTime = 0.25;
    public double tranPwr = 0.4;
    public boolean useTimerStop = true;//using a time-based transporter as opposed to detecting current

    public double angOffset; //this is jog
    public double resetAngle = 65;

    public double climbCannonAng = 55; //110 was for the passive climb

    public double sensorResetAngle = 50;
    public double sensorResetPwr = 0.07;
    public double sensorResetTime = 5;

    public Vector targetLocation = Vector.fromXY(0, 0);
    public double maxPower = 0.2;
    public EditableCal drivekR0 = new EditableCal("shootDrive0 kR", 0.02);
    public EditableCal drivekR45 = new EditableCal("shootDrive45 kR", 0.005);
    public EditableCal drivekD = new EditableCal("shootDrive kD", -0.0008, false);

    public boolean useAutoLoad = false;

    public EditableCal max2shootAngle = new EditableCal("Max2ShootAng", 25, false);

    public CalsCannon(){

    }
}
