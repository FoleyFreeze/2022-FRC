package frc.robot.Util.Motor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonMotor implements Motor{

    CalsMotor cals;
    TalonSRX motor;
    boolean brake;

    public TalonMotor(CalsMotor cals){
        this.cals = cals;
        motor = new WPI_TalonSRX(cals.channel);
    }

    public void setPower(double power){
        motor.set(ControlMode.PercentOutput, power);
    }

    public void setPosition(double revs){
        motor.set(ControlMode.Position, revs);
    }

    public double getPosition(){
        return motor.getSelectedSensorPosition() / cals.ticksPerUnit;
    }

    public boolean getBrake(){
        return brake;
    }

    public void setBrake(boolean brake){
        if(brake){
            motor.setNeutralMode(NeutralMode.Brake);
            this.brake = true;
        } else {
            motor.setNeutralMode(NeutralMode.Coast);
            this.brake = false;
        }
    }

    @Override
    public void close() throws Exception {
        throw new NoSuchMethodException();
    }

    @Override
    public void resetEncoder() {
        
    }

    @Override
    public void setSpeed(double speed) {
        
    }

    @Override
    public double getSpeed() {
        return motor.getSelectedSensorVelocity();
    }

    @Override
    public double getClosedLoopError() {
        return motor.getClosedLoopError();
    }
}
