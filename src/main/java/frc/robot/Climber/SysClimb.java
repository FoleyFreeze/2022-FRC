package frc.robot.Climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SysClimb extends SubsystemBase implements AutoCloseable{

    CalsClimb cals;

    public SysClimb(CalsClimb cals){
        this.cals = cals;
    }

    public void firstBar(){

    }

    public void secondBar(){

    }

    @Override
    public void periodic(){

    }

    @Override
    public void close() throws Exception {
        
    }
}
