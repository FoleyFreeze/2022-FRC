package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;

public class CmdShoot extends SequentialCommandGroup{
    
    RobotContainer r;

    public CmdShoot(RobotContainer r){
        this.r = r;
        addRequirements(r.cannon);
        addCommands(new SequentialCommandGroup(new CmdPrime(r), new CmdFire(r)));
    }
}
