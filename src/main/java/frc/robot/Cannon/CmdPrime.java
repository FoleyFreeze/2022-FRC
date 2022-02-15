package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdPrime extends CommandBase{

    RobotContainer r;

    public CmdPrime(RobotContainer r){
        this.r = r;
    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){
        r.cannon.prime();
    }

    @Override
    public void end(boolean interrupted){

    }

    @Override
    public boolean isFinished(){
        return r.cannon.upToSpeed();
    }
}
