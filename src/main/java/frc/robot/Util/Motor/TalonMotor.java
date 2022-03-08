package frc.robot.Util.Motor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class TalonMotor implements Motor{

    CalsMotor cals;
    TalonFX motor;
    boolean brake;

    public TalonMotor(CalsMotor cals){
        this.cals = cals;
        motor = new TalonFX(cals.channel);

        motor.setInverted(cals.invert);

        motor.config_kP(0, cals.kP);
        motor.config_kI(0, cals.kI);
        motor.config_kD(0, cals.kD);
        motor.config_kF(0, cals.kF);
        if(cals.izone > 0){
            motor.config_IntegralZone(0, cals.izone);
        }

        motor.configPeakOutputForward(cals.powerLimitMax);
        motor.configPeakOutputReverse(cals.powerLimitMin);

        motor.configClosedloopRamp(0.3);
    }

    public void setPower(double power){
        motor.set(ControlMode.PercentOutput, power);
    }

    public void setPosition(double revs){
        //motor.set(ControlMode.Position, revs);
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
        //convert to ticks per 100ms
        motor.set(ControlMode.Velocity, speed * 2048 / 600.0);
    }

    @Override
    public double getSpeed() {
        //convert to rpm from ticks per 100ms (2048 ticks per rev)
        return motor.getSelectedSensorVelocity() * 600 / 2048.0;
    }

    @Override
    public double getClosedLoopError() {
        //convert to rpm
        return motor.getClosedLoopError() * 600 / 2048.0;
    }

    @Override
    public void setEncoderPosition(double position) {
        
    }
}
