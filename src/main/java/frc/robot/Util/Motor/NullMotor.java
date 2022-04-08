package frc.robot.Util.Motor;

public class NullMotor implements Motor{

    @Override
    public void setPower(double power) {
    }

    @Override
    public void setPosition(double revs) {
    }

    @Override
    public double getPosition() {
        return 0;
    }

    @Override
    public boolean getBrake() {
        return false;
    }

    @Override
    public void setBrake(boolean brake) {
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void resetEncoder() {
        
    }

    @Override
    public void setSpeed(double speed) {
        
    }

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public double getClosedLoopError() {
        return 0;
    }

    @Override
    public void setEncoderPosition(double position) {
        
    }

    @Override
    public double getMotorSideCurrent() {
        return 0;
    }

    @Override
    public double getTemp() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setVoltage(double volts){
        
    }
    
}
