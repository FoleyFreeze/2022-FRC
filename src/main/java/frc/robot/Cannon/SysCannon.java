package frc.robot.Cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Interpolate;
import frc.robot.Util.Motor.Motor;

public class SysCannon extends SubsystemBase implements AutoCloseable{
    
    CalsCannon cals;
    
    Motor cwMotor;
    Motor ccwMotor;
    Motor angleMotor;
    Motor fireMotor;
    Motor transpMotor;

    double jogSpeed;
    double jogAng;

    public SysCannon(CalsCannon cals){
        this.cals = cals;
        if (cals.DISABLED) return;

        jogSpeed = cals.jogInitSpeed;
        jogAng = cals.jogInitAng;
        
        cwMotor = Motor.create(cals.cwMotor);
        ccwMotor = Motor.create(cals.ccwMotor);
        angleMotor = Motor.create(cals.angleMotor);
        fireMotor = Motor.create(cals.fireMotor);
        transpMotor = Motor.create(cals.transpMotor);
    }

    public void prime(){
        //manual prime from control board
        prime(jogSpeed + cals.LAYUP_SHOOT_SPEED, jogAng + cals.LAYUP_SHOOT_ANG);
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

        setAngle(angle);
        setSpeed(speed, speed);
    }

    public void fire(double power){
        fireMotor.setPower(power);
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
