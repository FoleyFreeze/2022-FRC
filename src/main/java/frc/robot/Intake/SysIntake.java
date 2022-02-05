package frc.robot.Intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Motor.Motor;

public class SysIntake extends SubsystemBase {
    
    CalsIntake cals;

    Motor leftMotor;
    Motor rightMotor;

    public SysIntake(CalsIntake cals){
        this.cals = cals;
        if(cals.DISABLED) return;
    }

    public void intake(double speed){
        leftMotor.setPower(speed * cals.RPM_TO_POWER);
        rightMotor.setPower(speed * cals.RPM_TO_POWER);
    }
}
