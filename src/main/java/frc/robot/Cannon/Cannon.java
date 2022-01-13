package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Cannon extends SubsystemBase {
    
    CalsCannon cals;

    public Cannon(CalsCannon cals){
        this.cals = cals;
    }
}
