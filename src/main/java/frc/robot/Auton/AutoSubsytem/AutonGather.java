package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.RobotContainer;
import frc.robot.Intake.CmdAutoGather;

public class AutonGather extends ParallelRaceGroup {
    public AutonGather(RobotContainer r){
        addCommands(new CmdAutoGather(r));
    }

    @Override
    public boolean isFinished(){
        super.isFinished();
        return false;
    }
}

//TODO: finish this, add init,exec,end,isfinished, etc
