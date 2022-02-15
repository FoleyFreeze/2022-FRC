package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

public class CmdFire extends WaitCommand{

    RobotContainer r;

    public CmdFire(RobotContainer r){
        super(r.cannon.cals.shootTime);
        this.r = r;
    }

    @Override
    public void execute(){
        r.cannon.prime();
        r.cannon.fire();
    }

    @Override
    public void end(boolean interrupted){
        super.end(interrupted);

        r.cannon.setSpeed(0, 0);
        r.cannon.fire(0);
    }
}
