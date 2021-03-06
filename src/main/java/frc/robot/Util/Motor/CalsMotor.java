package frc.robot.Util.Motor;

import frc.robot.Util.EditableCal;

public class CalsMotor {
    
    public enum MotorType{
        PWM, SPARK, TALON, NULL
    }
    public MotorType type;

    public int channel;

    public double powerLimitMax = 1;
    public double powerLimitMin = -1;

    public double ticksPerUnit = 1;

    public double kP = 0;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0;
    public double kIlim = 0;
    public double dFilt = 0;
    public double izone = 0;

    public EditableCal eCpowerLimitMax;
    public EditableCal eCpowerLimitMin;

    public EditableCal eCkP;
    public EditableCal eCkI;
    public EditableCal eCkD;
    public EditableCal eCkF;

    public boolean invert;
    public boolean brake;

    public double rampRate;

    public CalsMotor(MotorType t, int channel){
        type = t;
        this.channel = channel;
    }

    public CalsMotor setRamp(double r){
        rampRate = r;
        return this;
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

    public CalsMotor setPIDF(EditableCal p, EditableCal i, EditableCal d, EditableCal f){
        this.eCkP = p;
        this.eCkI = i;
        this.eCkD = d;
        this.eCkF = f;
        return this;
    }

    public CalsMotor setDfilt(double filt){
        this.dFilt = filt;
        return this;
    }

    public CalsMotor setIzone(double izone){
        this.izone = izone;
        return this;
    }

    public CalsMotor setPIDPwrLim(double max){
        this.powerLimitMax = max;
        this.powerLimitMin = -max;
        return this;
    }

    public CalsMotor setPIDPwrLim(double min, double max){
        powerLimitMax = max;
        powerLimitMin = min;
        return this;
    }

    public CalsMotor setPIDPwrLim(EditableCal max){
        eCpowerLimitMax = max;
        eCpowerLimitMin = null;
        return this;
    }

    public CalsMotor setPIDPwrLim(EditableCal min, EditableCal max){
        eCpowerLimitMax = max;
        eCpowerLimitMin = min;
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
