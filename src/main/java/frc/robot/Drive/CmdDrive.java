package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class CmdDrive extends CommandBase {
    
    SysDriveTrain m_subsystem;
    CalsDrive cals;

    public CmdDrive(CalsDrive cals){
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
