package frc.robot.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Gather extends CommandBase{
    
    Intake m_subsystem;
    CalsIntake cals;

    public Gather(CalsIntake cals){
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
