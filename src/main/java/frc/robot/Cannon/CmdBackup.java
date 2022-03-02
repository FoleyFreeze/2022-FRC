package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

public class CmdBackup extends WaitCommand{

    RobotContainer r;

    public CmdBackup(double time, RobotContainer r){
        super(time);
        this.r = r;
        addRequirements(r.cannon);
    }

    @Override
    public void execute(){
        super.execute();
        r.cannon.fire(0.2);
    }

    @Override
    public void end(boolean interrupted){
        super.end(interrupted);
        r.cannon.fire(0);
    }
}
