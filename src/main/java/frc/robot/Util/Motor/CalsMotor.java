package frc.robot.Util.Motor;

public class CalsMotor {
    
    public enum MotorType{
        PWM, SPARK, TALON, NULL
    }
    public MotorType type;

    public int channel;

    public double powerLimit = 1;

    public double ticksPerUnit = 1;

    public double kP = 0;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0;
    public double kIlim = 0;
    public double dFilt = 0;

    public boolean invert;
    public boolean brake;

    public CalsMotor(MotorType t, int channel){
        type = t;
        this.channel = channel;
    }

    public CalsMotor setEncUnits(double ticksPerUnit){
        this.ticksPerUnit = ticksPerUnit;
        return this;
    }

    public CalsMotor setPIDF(double p, double i, double d, double f){
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kF = f;
        return this;
    }

    public CalsMotor setDfilt(double filt){
        this.dFilt = filt;
        return this;
    }

    public CalsMotor setPIDPwrLim(double max){
        this.powerLimit = max;
        return this;
    }

    public CalsMotor invert(){
        this.invert = true;
        return this;
    }

    public CalsMotor brake(){
        this.brake = true;
        return this;
    }

    public CalsMotor setkIlim(double iLim){
        this.kIlim = iLim;
        return this;
    }
}
