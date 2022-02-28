package frc.robot.Climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Motor.Motor;

public class SysClimb extends SubsystemBase implements AutoCloseable{

    CalsClimb cals;
    Motor climbArms;
    Motor climbWinch;

    public SysClimb(CalsClimb cals){
        this.cals = cals;
        if (cals.DISABLED) return;

        climbArms = Motor.create(cals.climbArms);
        climbWinch = Motor.create(cals.climbWinch);
    }

    @Override
    public void periodic(){
        if (cals.DISABLED) return;

    }

    @Override
    public void close() throws Exception {
        climbArms.close();
        climbWinch.close();
    }
}
