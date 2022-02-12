package frc.robot.Intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Motor.Motor;

public class SysIntake extends SubsystemBase implements AutoCloseable{
    
    CalsIntake cals;

    Motor intakeMotor;

    public SysIntake(CalsIntake cals){
        this.cals = cals;
        if(cals.DISABLED) return;
    }

    public void intake(double speed){
        intakeMotor.setPower(speed);
    }

    public void intake(){
        intakeMotor.setPower(cals.intakeSpeed);
    }

    public void reverse(){
        intakeMotor.setPower(cals.reverseSpeed);
    }

    public void stop(){
        intakeMotor.setPower(0);
    }

    @Override
    public void close() throws Exception {
        
    }
}
