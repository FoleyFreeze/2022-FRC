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
    }

    public void setPower(double power){
        motor.set(power);
    }

    public void setPosition(double revs){
        pidController.setReference(revs, ControlType.kPosition);
    }

    public double getPosition(){
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
