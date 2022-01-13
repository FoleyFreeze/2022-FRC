package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Shoot extends CommandBase {
    
    Cannon m_subsystem;
    CalsCannon cals;

    public Shoot(CalsCannon cals){
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
