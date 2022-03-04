package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;

public class CmdLoadSequential extends SequentialCommandGroup{

    public CmdLoadSequential(RobotContainer r){
        addCommands(new SequentialCommandGroup(new CmdTransport(r), new CmdKickerLoad(0.25, r)));
    }
}
