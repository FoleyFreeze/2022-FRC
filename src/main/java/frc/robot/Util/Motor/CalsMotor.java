package frc.robot.Util.Motor;

public class CalsMotor {
    
    public enum MotorType{
        PWM, SPARK, TALON, NULL
    }
    public MotorType type;

    public int channel;

    public double ticksPerUnit;

    public double p;
    public double i;
    public double d;
    public double f;

    public CalsMotor(MotorType t, int channel){
        type = t;
        this.channel = channel;
    }

    public CalsMotor setEncUnits(double ticksPerUnit){
        this.ticksPerUnit = ticksPerUnit;
        return this;
    }

    public CalsMotor setPIDF(double p, double i, double d, double f){
        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;
        return this;
    }
}
