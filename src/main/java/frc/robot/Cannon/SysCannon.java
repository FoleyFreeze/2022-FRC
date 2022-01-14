package frc.robot.Cannon;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SysCannon extends SubsystemBase {
    
    CalsCannon cals;
    
    Spark shootMotor;

    public SysCannon(CalsCannon cals){
        this.cals = cals;
        shootMotor = new Spark(cals.SHOOT_MOTOR_CHANNEL);
    }

    public void prime(){

    }

    public void fire(){

    }
}
