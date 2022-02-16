package frc.robot.Cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Util.Interpolate;
import frc.robot.Util.Motor.Motor;

public class SysCannon extends SubsystemBase implements AutoCloseable{
    
    CalsCannon cals;
    RobotContainer r;
    
    Motor cwMotor;
    Motor ccwMotor;
    Motor angleMotor;
    Motor fireMotor;
    Motor transpMotor;

    double jogSpeed;
    double jogAng;

    public SysCannon(CalsCannon cals, RobotContainer r){
        this.cals = cals;
        if (cals.DISABLED) return;
        this.r = r;

        jogSpeed = cals.jogInitSpeed;
        jogAng = cals.jogInitAng;
        
        cwMotor = Motor.create(cals.cwMotor);
        ccwMotor = Motor.create(cals.ccwMotor);
        angleMotor = Motor.create(cals.angleMotor);
        fireMotor = Motor.create(cals.fireMotor);
        transpMotor = Motor.create(cals.transpMotor);
    }

    public void prime(){
        if(r.inputs.cameraDrive()){
            prime(r.sensors.target.location.r, true);
        }else{
            prime(cals.LAYUP_SHOOT_SPEED, cals.LAYUP_SHOOT_ANG);
        }
    }

    public void prime(double dist, boolean spinIt){
        if (cals.DISABLED) return;
        
        double speed = 0;
        if(spinIt) {
            speed = Interpolate.interpolate(cals.distances, cals.speeds, dist);
        }
        double angle = Interpolate.interpolate(cals.distances, cals.angles, dist);

        prime(speed, angle);
    }

    public void prime(double speed, double angle){
        if (cals.DISABLED) return;

        setAngle(angle + jogAng);
        setSpeed(speed + jogSpeed, speed + jogSpeed);
    }

    public void fire(double power){
        fireMotor.setPower(power);
    }

    public void fire(){
        fire(cals.wheelOfFirePower);
    }

    public void setAngle(double angle){
        if (cals.DISABLED) return;
        
        if (angle > cals.shootMaxAngle)
            angle = cals.shootMaxAngle;

        if (angle < cals.shootMinAngle)
            angle = cals.shootMinAngle;

        double revs = angle / 360;
        angleMotor.setPosition(revs);
    }

    //sets motors via speeds in RPM
    public void setSpeed(double ccwSpeed, double cwSpeed){
        if (cals.DISABLED) return;

        ccwMotor.setSpeed(ccwSpeed);
         cwMotor.setSpeed(cwSpeed);
    }

    public void setPower(double ccwPower, double cwPower){
        if(cals.DISABLED) return;

        ccwMotor.setPower(ccwPower);
         cwMotor.setPower(cwPower);
    }

    public boolean upToSpeed(){
        double error = ccwMotor.getClosedLoopError();
        error += cwMotor.getClosedLoopError();

        return error > cals.minShootSpeedError && error < cals.maxShootSpeedError;
    }

    public void jogSpeed(boolean up){
        if(up){
            jogSpeed += cals.jogSpeedInterval;
        } else {
            jogSpeed -= cals.jogSpeedInterval;
        }
    }

    public void jogAng(boolean right){
        if(right){
            jogAng += cals.jogAngInterval;
        } else {
            jogAng -= cals.jogAngInterval;
        }
    }

    public void transport(){
        transpMotor.setPower(cals.tranSpeed);
    }

    public void stopTransport(){
        transpMotor.setPower(0);
    }

    private boolean cargoReady;
    public void preLoadCargo(){
        cargoReady = true;
    }

    private double preLoadTimer;
    public void periodic(){
        if(cargoReady){
            cargoReady = false;
            preLoadTimer = Timer.getFPGATimestamp() + cals.preLoadTime;
        }
        if(Timer.getFPGATimestamp() < preLoadTimer){
            transport();
        } else {
            stopTransport();
        }
    }

    @Override
    public void close() throws Exception {
        
    }
}
