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
    }

    public double[] angleEncoderOffsets = {0.0, 0.0, 0.0, 0.0};

    public CalsWheel FLwheel = new CalsWheel();{
        FLwheel.driveMotor = new CalsMotor(MotorType.SPARK, 0).setEncUnits(0);
        FLwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 1).setEncUnits(0).setPIDF(0, 0, 0, 0);
        FLwheel.angleEncoderChannel = 0;
        FLwheel.angleEncoderOffset = angleEncoderOffsets[0];
        FLwheel.wheelLocation = Vector.fromXY(0, 0);
    }

    public CalsWheel FRwheel = new CalsWheel();{
        FRwheel.driveMotor = new CalsMotor(MotorType.SPARK, 2).setEncUnits(0);
        FRwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 3).setEncUnits(0).setPIDF(0, 0, 0, 0);
        FRwheel.angleEncoderChannel = 1;
        FRwheel.angleEncoderOffset = angleEncoderOffsets[1];
        FRwheel.wheelLocation = Vector.fromXY(0, 0);
    }

    public CalsWheel RLwheel = new CalsWheel();{
        RLwheel.driveMotor = new CalsMotor(MotorType.SPARK, 4).setEncUnits(0);
        RLwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 5).setEncUnits(0).setPIDF(0, 0, 0, 0);
        RLwheel.angleEncoderChannel = 2;
        RLwheel.angleEncoderOffset = angleEncoderOffsets[2];
        RLwheel.wheelLocation = Vector.fromXY(0, 0);
    }

    public CalsWheel RRwheel = new CalsWheel();{
        RRwheel.driveMotor = new CalsMotor(MotorType.SPARK, 6).setEncUnits(0);
        RRwheel.swerveMotor = new CalsMotor(MotorType.SPARK, 7).setEncUnits(0).setPIDF(0, 0, 0, 0);
        RRwheel.angleEncoderChannel = 3;
        RRwheel.angleEncoderOffset = angleEncoderOffsets[3];
        RRwheel.wheelLocation = Vector.fromXY(0, 0);
    }

    public CalsWheel[] wheelCals = {FLwheel, FRwheel, RLwheel, RRwheel};

    Vector defaultRobotCenter = Vector.fromXY(0, 0);

    public CalsDrive(){
                
        
    }
}
