package frc.robot.Util.Motor;

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

    public SparkMotor(CalsMotor cals){
        this.cals = cals;
        motor = new CANSparkMax(cals.channel, MotorType.kBrushless);
        pidController = motor.getPIDController();
        encoder = motor.getEncoder();

        pidController.setP(cals.kP);
        pidController.setI(cals.kI);
        pidController.setD(cals.kD);
        pidController.setFF(cals.kF);
        pidController.setDFilter(cals.dFilt);

        motor.setInverted(cals.invert);
        setBrake(cals.brake);

        //this is in units/tick
        //TODO(1): do this later, but for now the 2019 bot assumes conversion outside the spark
        //encoder.setPositionConversionFactor(1 / cals.ticksPerUnit);

        pidController.setOutputRange(-cals.powerLimit, cals.powerLimit);
    }

    public void setPower(double power){
        motor.set(power);
    }

    public void setPosition(double revs){
        //TODO(1): remove the scaling
        pidController.setReference(revs * cals.ticksPerUnit, ControlType.kPosition);
    }

    public double getPosition(){
        ////TODO(1): remove the scaling
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
}
