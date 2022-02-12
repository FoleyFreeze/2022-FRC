package frc.robot.Intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Motor.Motor;

public class SysIntake extends SubsystemBase implements AutoCloseable{
    
    CalsIntake cals;

    Motor leftMotor;
    Motor rightMotor;

    public SysIntake(CalsIntake cals){
        this.cals = cals;
        if(cals.DISABLED) return;
    }

    public void intake(double speed){
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(-speed);
    }

    @Override
    public void close() throws Exception {
        
    }
}
