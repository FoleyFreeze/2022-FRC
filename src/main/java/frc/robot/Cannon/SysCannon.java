package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Interpolate;
import frc.robot.Util.Motor.Motor;

public class SysCannon extends SubsystemBase {
    
    CalsCannon cals;
    
    Motor cwMotor;
    Motor ccwMotor;
    Motor angleMotor;

    public SysCannon(CalsCannon cals){
        this.cals = cals;
        if (cals.DISABLED) return;
        
        cwMotor = Motor.create(cals.cwMotor);
        ccwMotor = Motor.create(cals.ccwMotor);
        angleMotor = Motor.create(cals.angleMotor);
    }

    public void prime(double dist){
        if (cals.DISABLED) return;

        double speed = Interpolate.interpolate(cals.distances, cals.speeds, dist);
        double angle = Interpolate.interpolate(cals.distances, cals.angles, dist);

        prime(speed, angle);
    }

    public void prime(double speed, double angle){
        if (cals.DISABLED) return;

        setAngle(angle);
        setSpeed(speed, speed);
    }

    public void fire(){
        //once we figure this out, this will just use a mechanism to push the ball into the shooter
    }

    public void setAngle(double angle){
        if (cals.DISABLED) return;

        double revs = angle / 360;
        angleMotor.setPosition(revs);
    }

    //sets motors via speeds in RPM
    public void setSpeed(double ccwSpeed, double cwSpeed){
        if (cals.DISABLED) return;

        ccwMotor.setPower(ccwSpeed * cals.RPM_TO_POWER);
        cwMotor.setPower(cwSpeed * cals.RPM_TO_POWER);
    }
}
