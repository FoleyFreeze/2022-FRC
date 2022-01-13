package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveTrain extends SubsystemBase {
    
    CalsDrive cals;

    public DriveTrain(CalsDrive cals){
        this.cals = cals;
    }
}
