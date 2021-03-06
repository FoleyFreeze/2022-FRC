package frc.robot.Util.Motor;

import edu.wpi.first.wpilibj.RobotBase;

public interface Motor extends AutoCloseable {

    public static Motor create(CalsMotor c){
        if(RobotBase.isSimulation()){
            return new NullMotor();
        }
        switch(c.type){
            case PWM:
                return new PWMMotor(c);
            case SPARK:
                return new SparkMotor(c);
            case TALON:
                return new TalonMotor(c);
            case NULL:
            default:
                return new NullMotor();
        }
    }

    public void setPower(double power);
    public void setPosition(double revs);
    public double getPosition();
    public boolean getBrake();
    public void setBrake(boolean brake);
    public void resetEncoder();
    public void setSpeed(double speed);
    public double getSpeed();
    public double getClosedLoopError();
    public void setEncoderPosition(double revs);
    public double getMotorSideCurrent();
    public double getTemp();
    public void setVoltage(double volts);
}