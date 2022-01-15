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
    
}
