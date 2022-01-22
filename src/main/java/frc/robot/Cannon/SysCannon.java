package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Motor.Motor;

public class SysCannon extends SubsystemBase {
    
    CalsCannon cals;
    
    Motor shootMotor;

    public SysCannon(CalsCannon cals){
        this.cals = cals;
    }

    public void prime(){

    }

    public void fire(){

    }
}
