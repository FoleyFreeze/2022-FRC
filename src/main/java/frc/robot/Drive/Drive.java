package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {
    
    CalsDrive cals;

    public Drive(CalsDrive cals){
        this.cals = cals;
    }
}
