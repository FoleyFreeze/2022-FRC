package frc.robot.Vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {

    CalsVision cals;

    public Vision(CalsVision cals){
        this.cals = cals;
    }
}
