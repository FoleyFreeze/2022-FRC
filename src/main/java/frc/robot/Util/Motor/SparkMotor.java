package frc.robot.Util.Motor;

import java.util.function.DoubleConsumer;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class SparkMotor implements Motor{

    CalsMotor cals;
    CANSparkMax motor;
    SparkMaxPIDController pidController;
    RelativeEncoder encoder;

    enum SetpointType{
        POWER, POSITION, VELOCITY
    }
    private SetpointType setpointType = SetpointType.POWER;
    private double setpoint;

    public SparkMotor(CalsMotor cals){
        this.cals = cals;
        motor = new CANSparkMax(cals.channel, MotorType.kBrushless);
        pidController = motor.getPIDController();
        encoder = motor.getEncoder();

        if(cals.eCkP != null){
            pidController.setP(cals.eCkP.get());
            cals.eCkP.addCallback(new DoubleConsumer() {
                public void accept(double d){
                    pidController.setP(d);
                }
            });
            pidController.setI(cals.eCkI.get());
            cals.eCkP.addCallback(new DoubleConsumer() {
                public void accept(double d){
                    pidController.setI(d);
                }
            });
            pidController.setD(cals.eCkD.get());
            cals.eCkP.addCallback(new DoubleConsumer() {
                public void accept(double d){
                    pidController.setD(d);
                }
            });
            pidController.setFF(cals.eCkF.get());
            cals.eCkP.addCallback(new DoubleConsumer() {
                public void accept(double d){
                    pidController.setFF(d);
                }
            });
        } else {
            pidController.setP(cals.kP);
            pidController.setI(cals.kI);
            pidController.setD(cals.kD);
            pidController.setFF(cals.kF);
        }
        pidController.setIZone(cals.kIlim);
        pidController.setDFilter(cals.dFilt);

        motor.setInverted(cals.invert);

        setBrake(cals.brake);

        //this is in units/tick
        //encoder.setPositionConversionFactor(1 / cals.ticksPerUnit);

        if(cals.eCpowerLimitMax != null){
            cals.powerLimitMax = cals.eCpowerLimitMax.get();
            if(cals.eCpowerLimitMin == null){
                cals.powerLimitMin = -cals.eCpowerLimitMax.get();
                cals.eCpowerLimitMax.addCallback(new DoubleConsumer() {
                    public void accept(double d){
                        pidController.setOutputRange(-d, d);
                    }
                });
            } else {
                cals.powerLimitMin = cals.eCpowerLimitMin.get();
                cals.eCpowerLimitMax.addCallback(new DoubleConsumer() {
                    public void accept(double d){
                        cals.powerLimitMax = d;
                        pidController.setOutputRange(cals.powerLimitMin, cals.powerLimitMax);
                    }
                });
                cals.eCpowerLimitMin.addCallback(new DoubleConsumer() {
                    public void accept(double d){
                        cals.powerLimitMin = d;
                        pidController.setOutputRange(cals.powerLimitMin, cals.powerLimitMax);
                    }
                });
            }
        }
        pidController.setOutputRange(cals.powerLimitMin, cals.powerLimitMax);

        motor.setOpenLoopRampRate(cals.rampRate);
        motor.setClosedLoopRampRate(cals.rampRate);
    }

    public void setPower(double power){
        motor.set(power);
        setpoint = 0;
        setpointType = SetpointType.POWER;
    }

    public void setPosition(double revs){
        //TODO(1): remove the scaling
        pidController.setReference(revs * cals.ticksPerUnit, ControlType.kPosition);
        setpoint = revs;
        setpointType = SetpointType.POSITION;
    }

    public double getPosition(){
        //TODO(1): remove the scaling
        return encoder.getPosition() / cals.ticksPerUnit;
    }

    public boolean getBrake(){
        return motor.getIdleMode() == IdleMode.kBrake;
    }

    public void setBrake(boolean brake){
        if(brake){
            motor.setIdleMode(IdleMode.kBrake);
        } else {
            motor.setIdleMode(IdleMode.kCoast);
        }
    }

    @Override
    public void close() throws Exception {
        motor.close();
    }

    @Override
    public void resetEncoder() {
        encoder.setPosition(0);
    }

    @Override
    public double getClosedLoopError(){
        switch(setpointType){
            case POSITION:
                return getPosition() - setpoint;
            case POWER:
                return 0;
            case VELOCITY:
                return getSpeed() - setpoint;
        }
        return 0;
    }

    @Override
    public void setSpeed(double speed) {
        //need to convert speed in units/sec to RPM
        pidController.setReference(speed * cals.ticksPerUnit * 60, ControlType.kVelocity);
        setpoint = speed;
        setpointType = SetpointType.VELOCITY;
    }

    @Override
    public double getSpeed(){
        return encoder.getVelocity() / cals.ticksPerUnit / 60.0;
    }

    @Override
    public void setEncoderPosition(double position){
        encoder.setPosition(position * cals.ticksPerUnit);
    }

    @Override
    public double getMotorSideCurrent() {
        return 0;
    }

    @Override
    public double getTemp() {
        return motor.getMotorTemperature();
    }

    @Override
    public void setVoltage(double volts){
        motor.setVoltage(volts);
    }
}
