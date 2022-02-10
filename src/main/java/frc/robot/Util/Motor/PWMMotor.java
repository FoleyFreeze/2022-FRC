package frc.robot.Util.Motor;

import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class PWMMotor implements Motor{

    Spark motor;

    public PWMMotor(CalsMotor cals){
        motor = new Spark(cals.channel);
    }

    public void setPower(double power){
        motor.set(power);
    }

    public void setPosition(double revs){
        
    }

    public double getPosition(){
        return 0;
    }

    public boolean getBrake(){
        return false;
    }

    public void setBrake(boolean brake){
        
    }

    @Override
    public void close() throws Exception {
        motor.close();
    }

    @Override
    public void resetEncoder() {
        
    }

    @Override
    public void setSpeed(double speed) {
        
    }
}
