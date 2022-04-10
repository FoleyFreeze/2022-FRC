package frc.robot.Intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Motor.Motor;

public class SysIntake extends SubsystemBase implements AutoCloseable{
    
    public CalsIntake cals;

    public Motor intakeMotor;

    public SysIntake(CalsIntake cals){
        this.cals = cals;
        if(cals.DISABLED) return;
        intakeMotor = Motor.create(cals.intakeMotor);
    }

    public void intake(double pwr){
        if (cals.DISABLED) return;
        intakeMotor.setPower(pwr);
    }

    public void intake(){
        if (cals.DISABLED) return;
        intakeMotor.setPower(cals.intakeSpeed);
    }

    public void reverse(){
        if (cals.DISABLED) return;
        intakeMotor.setPower(cals.reverseSpeed);
    }

    public void stop(){
        intakeMotor.setPower(0);
    }

    @Override
    public void close() throws Exception {
        intakeMotor.close();
    }
}
