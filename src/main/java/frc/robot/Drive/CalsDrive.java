package frc.robot.Drive;

import frc.robot.Robot;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;


public class CalsDrive {
    
    public static class CalsWheel {
        public CalsMotor driveMotor;
        public CalsMotor swerveMotor;
        public int angleEncoderChannel;
        public double angleEncoderOffset;
        public Vector wheelLocation;
        public String name;

        public double maxPower;

        public double driveInPerSwerveRotation = -32/18.0 * 15/45.0 * 4*Math.PI;

        public boolean useVelocityControl = true;
        public double maxVelocity = 120;//inches per second
    }

    @SuppressWarnings("unused")
    public final boolean DISABLED = false && Robot.isReal();

    //Swerve Motor PID
    public double[] angleEncoderOffsets = {4.896, 1.298, 0.544, 2.170};
    double kPsw = 0.2;
    double kIsw = 0.005;
    double kDsw = 0.3;
    double kFsw = 0.0;
    double iLimsw = 3;
    double dFiltsw = 0.00;
    public  double maxSwervePwr = 0.6;
    public final double MAX_FIELD_PWR = 0.6;
    public final double MAX_PIT_PWR = 0.2;
    double swerveRotationsPer360 = 60.0;

    //Drive Motor PID
    double kPdr = 0.00015;
    double kIdr = 5e-7;
    double kDdr = 0;
    double kFdr = 0.00017;
    double iLimdr = 0;
    double dFiltdr = 0;
    public final static double MAX_DRIVE_PWR = 0.6;
    double driveRotationsPerInch = 64/18.0 * 18/32.0 * 45/15.0 / 4.0 / Math.PI;

    public boolean fieldOriented;
    
    public double xWheelPos = 10.75;
    public double yWheelPos = 12.5;

    public CalsWheel FLwheel = new CalsWheel();{
        FLwheel.driveMotor = new CalsMotor(MotorType.SPARK, 20).setEncUnits(driveRotationsPerInch).invert().setPIDF(kPdr,kIdr,kDdr,kFdr).setPIDPwrLim(MAX_DRIVE_PWR);
        FLwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 1).setEncUnits(swerveRotationsPer360).setPIDF(kPsw, kIsw, kDsw, kFsw).setkIlim(iLimsw).setPIDPwrLim(maxSwervePwr).brake();
        FLwheel.angleEncoderChannel = 1;
        FLwheel.angleEncoderOffset = angleEncoderOffsets[FLwheel.angleEncoderChannel];
        FLwheel.wheelLocation = Vector.fromXY(-xWheelPos, yWheelPos);
        FLwheel.maxPower = MAX_DRIVE_PWR;
        FLwheel.name = "FL";
    }

    public CalsWheel FRwheel = new CalsWheel();{
        FRwheel.driveMotor = new CalsMotor(MotorType.SPARK, 18).setEncUnits(driveRotationsPerInch).invert().setPIDF(kPdr,kIdr,kDdr,kFdr).setPIDPwrLim(MAX_DRIVE_PWR);
        FRwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 19).setEncUnits(swerveRotationsPer360).setPIDF(kPsw, kIsw, kDsw, kFsw).setkIlim(iLimsw).setPIDPwrLim(maxSwervePwr).brake();
        FRwheel.angleEncoderChannel = 2;
        FRwheel.angleEncoderOffset = angleEncoderOffsets[FRwheel.angleEncoderChannel];
        FRwheel.wheelLocation = Vector.fromXY(xWheelPos, yWheelPos);
        FRwheel.maxPower = MAX_DRIVE_PWR;
        FRwheel.name = "FR";
    }

    public CalsWheel RLwheel = new CalsWheel();{
        RLwheel.driveMotor = new CalsMotor(MotorType.SPARK, 8).setEncUnits(driveRotationsPerInch).invert().setPIDF(kPdr,kIdr,kDdr,kFdr).setPIDPwrLim(MAX_DRIVE_PWR);
        RLwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 9).setEncUnits(swerveRotationsPer360).setPIDF(kPsw, kIsw, kDsw, kFsw).setkIlim(iLimsw).setPIDPwrLim(maxSwervePwr).brake();
        RLwheel.angleEncoderChannel = 0;
        RLwheel.angleEncoderOffset = angleEncoderOffsets[RLwheel.angleEncoderChannel];
        RLwheel.wheelLocation = Vector.fromXY(-xWheelPos, -yWheelPos);
        RLwheel.maxPower = MAX_DRIVE_PWR;
        RLwheel.name = "RL";
    }

    public CalsWheel RRwheel = new CalsWheel();{
        RRwheel.driveMotor = new CalsMotor(MotorType.SPARK, 10).setEncUnits(driveRotationsPerInch).invert().setPIDF(kPdr,kIdr,kDdr,kFdr).setPIDPwrLim(MAX_DRIVE_PWR);
        RRwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 11).setEncUnits(swerveRotationsPer360).setPIDF(kPsw, kIsw, kDsw, kFsw).setkIlim(iLimsw).setPIDPwrLim(maxSwervePwr).brake();
        RRwheel.angleEncoderChannel = 3;
        RRwheel.angleEncoderOffset = angleEncoderOffsets[RRwheel.angleEncoderChannel];
        RRwheel.wheelLocation = Vector.fromXY(xWheelPos, -yWheelPos);
        RRwheel.maxPower = MAX_DRIVE_PWR;
        RRwheel.name = "RR";
    }

    public CalsWheel[] wheelCals = {FLwheel, FRwheel, RLwheel, RRwheel};

    Vector defaultRobotCenter = Vector.fromXY(0, 0);

    public double minAutoPosError = 0;
    public double minAutoAngError = 0;

    public CalsDrive(){
                
        
    }
}
