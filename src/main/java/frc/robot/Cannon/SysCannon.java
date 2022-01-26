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
        cwMotor = Motor.create(cals.cwMotor);
        ccwMotor = Motor.create(cals.ccwMotor);
        angleMotor = Motor.create(cals.angleMotor);
        this.cals = cals;
    }

    public void prime(double dist){
        double speed = Interpolate.interpolate(cals.distances, cals.speeds, dist);
        double angle = Interpolate.interpolate(cals.distances, cals.angles, dist);

        prime(speed, angle);
    }

    public void prime(double speed, double angle){
        setAngle(angle);
        setSpeed(speed, speed);
    }

    public void fire(){

    }

    public void setAngle(double angle){
        double revs = angle / 360;
        angleMotor.setPosition(revs);
    }

    //sets motors via speeds in RPM
    public void setSpeed(double ccwSpeed, double cwSpeed){
        ccwMotor.setPower(ccwSpeed * cals.RPM_TO_POWER);
        cwMotor.setPower(cwSpeed * cals.RPM_TO_POWER);
    }
}
