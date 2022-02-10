package frc.robot.Util.Motor;

import edu.wpi.first.wpilibj.RobotBase;

public interface Motor extends AutoCloseable {

    public static Motor create(CalsMotor c){
        switch(c.type){
            case PWM:
                return new PWMMotor(c);
            case SPARK:
                return new SparkMotor(c);
            case TALON:
                if(RobotBase.isSimulation()){
                    return new PWMMotor(c);
                }
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
    
    /*
    public void setSpeed(double rpm);
    public double getSpeed();
    public double getCurrent();
    public double getTemp();
    public boolean isJammed();
    */
}