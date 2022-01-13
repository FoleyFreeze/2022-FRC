package frc.robot.Intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    
    CalsIntake cals;

    public Intake(CalsIntake cals){
        this.cals = cals;
    }
}
