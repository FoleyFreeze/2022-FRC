package frc.robot.Auton;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.RobotContainer;
import frc.robot.Intake.CmdAutoGather;

public class AutonGather extends ParallelRaceGroup {
    public AutonGather(RobotContainer r){
        addCommands(new CmdAutoGather(r));
    }
}

//TODO: finish this, add init,exec,end,isfinished, etc
