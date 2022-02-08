package frc.robot.Drive;

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
    }

    public final boolean DISABLED = false;

    public double[] angleEncoderOffsets = {4.896, 1.298, 0.544, 2.170};
    double kP = 0.1;
    double kI = 0.0;
    double kD = 0.0;
    double kF = 0.0;
    double dFilt = 0.00;
    public final double MAX_SWERVE_PWR = 0.6;
    public final double MAX_DRIVE_PWR = 0.15;
    double swerveRotationsPer360 = 60.0;
    double driveRotationsPerInch = 64/18.0 * 18/32.0 * 45/15.0 / 4.0 / Math.PI;

    public boolean fieldOriented;
    
    public double xWheelPos = 10.75;
    public double yWheelPos = 12.5;

    public CalsWheel FLwheel = new CalsWheel();{
        FLwheel.driveMotor = new CalsMotor(MotorType.SPARK, 20).setEncUnits(driveRotationsPerInch).invert();
        FLwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 5).setEncUnits(swerveRotationsPer360).setPIDF(kP, kI, kD, kF).setPIDPwrLim(MAX_SWERVE_PWR);
        FLwheel.angleEncoderChannel = 2;
        FLwheel.angleEncoderOffset = angleEncoderOffsets[FLwheel.angleEncoderChannel];
        FLwheel.wheelLocation = Vector.fromXY(-xWheelPos, yWheelPos);
        FLwheel.maxPower = MAX_DRIVE_PWR;
        FLwheel.name = "FL";
    }

    public CalsWheel FRwheel = new CalsWheel();{
        FRwheel.driveMotor = new CalsMotor(MotorType.SPARK, 1).setEncUnits(driveRotationsPerInch).invert();
        FRwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 4).setEncUnits(swerveRotationsPer360).setPIDF(kP, kI, kD, kF).setPIDPwrLim(MAX_SWERVE_PWR);
        FRwheel.angleEncoderChannel = 1;
        FRwheel.angleEncoderOffset = angleEncoderOffsets[FRwheel.angleEncoderChannel];
        FRwheel.wheelLocation = Vector.fromXY(xWheelPos, yWheelPos);
        FRwheel.maxPower = MAX_DRIVE_PWR;
        FRwheel.name = "FR";
    }

    public CalsWheel RLwheel = new CalsWheel();{
        RLwheel.driveMotor = new CalsMotor(MotorType.SPARK, 14).setEncUnits(driveRotationsPerInch).invert();
        RLwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 10).setEncUnits(swerveRotationsPer360).setPIDF(kP, kI, kD, kF).setPIDPwrLim(MAX_SWERVE_PWR);
        RLwheel.angleEncoderChannel = 3;
        RLwheel.angleEncoderOffset = angleEncoderOffsets[RLwheel.angleEncoderChannel];
        RLwheel.wheelLocation = Vector.fromXY(-xWheelPos, -yWheelPos);
        RLwheel.maxPower = MAX_DRIVE_PWR;
        RLwheel.name = "RL";
    }

    public CalsWheel RRwheel = new CalsWheel();{
        RRwheel.driveMotor = new CalsMotor(MotorType.SPARK, 15).setEncUnits(driveRotationsPerInch).invert();
        RRwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 11).setEncUnits(swerveRotationsPer360).setPIDF(kP, kI, kD, kF).setPIDPwrLim(MAX_SWERVE_PWR);
        RRwheel.angleEncoderChannel = 0;
        RRwheel.angleEncoderOffset = angleEncoderOffsets[RRwheel.angleEncoderChannel];
        RRwheel.wheelLocation = Vector.fromXY(xWheelPos, -yWheelPos);
        RRwheel.maxPower = MAX_DRIVE_PWR;
        RRwheel.name = "RR";
    }

    public CalsWheel[] wheelCals = {FLwheel, FRwheel, RLwheel, RRwheel};

    Vector defaultRobotCenter = Vector.fromXY(0, 0);

    public CalsDrive(){
                
        
    }
}
