package frc.robot.Util;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class InstantAlwaysCommand extends InstantCommand {

    public InstantAlwaysCommand(Runnable toRun){
        super(toRun);
    }

    @Override
    public boolean runsWhenDisabled(){
        return true;
    }
}
