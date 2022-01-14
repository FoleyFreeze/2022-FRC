package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Vector;

public class SysDriveTrain extends SubsystemBase {
    
    CalsDrive cals;

    Vector driveVec;

    public SysDriveTrain(CalsDrive cals){
        this.cals = cals;
    }

    public void periodic(){
        
    }
}
