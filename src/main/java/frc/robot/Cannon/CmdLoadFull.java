package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;

public class CmdLoadFull extends SequentialCommandGroup{
    
    RobotContainer r;

    CmdLoadFull(RobotContainer r){
        this.r = r;
    }

    
}
