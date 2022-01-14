package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class CmdShoot extends CommandBase {
    
    SysCannon m_subsystem;
    CalsCannon cals;

    public CmdShoot(CalsCannon cals){
        this.cals = cals;
    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){

    }

    @Override
    public void end(boolean interrupted){

    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
