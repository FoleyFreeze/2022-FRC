package frc.robot.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdManualClimb extends CommandBase {
    
    RobotContainer r;

    public CmdManualClimb(RobotContainer r){
        this.r = r;

    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){
        //winch is down
        if(r.inputs.driverJoy.intakeSpin()){
            r.climb.driveWinch(r.inputs.driverJoy.getDial3());
            r.climb.driveArms(0);
            //r.climb.climbWinch.setBrake(true);
        //arms are up
        } else if(r.inputs.driverJoy.fireSpin()){
            r.climb.driveArms(r.inputs.driverJoy.getDial3());
            r.climb.driveWinch(0);
            //r.climb.climbWinch.setBrake(false);
        } else {
            r.climb.driveArms(0);
            r.climb.driveWinch(0);
        }
    }

    @Override
    public void end(boolean interrupted){
        r.climb.driveArms(0);
        r.climb.driveWinch(0);
    }

    @Override
    public boolean isFinished(){
        return false;
    }

}
