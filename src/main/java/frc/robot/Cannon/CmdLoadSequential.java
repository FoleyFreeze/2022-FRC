package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;

public class CmdLoadSequential extends SequentialCommandGroup{

    public CmdLoadSequential(RobotContainer r){
        addRequirements(r.cannon);
        addCommands(new SequentialCommandGroup(new CmdLoad(r), new CmdBackup(0.1, r)));
    }
}
