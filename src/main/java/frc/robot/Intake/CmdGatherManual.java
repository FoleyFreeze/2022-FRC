package frc.robot.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdGatherManual extends CommandBase{

    RobotContainer r;

    public CmdGatherManual(RobotContainer r){
        this.r = r;
        addRequirements(r.intake);
        addRequirements(r.cannon);
    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){
        boolean trig = r.inputs.gather.get();
        if(r.inputs.intakeSpin.get()){
            r.intake.intake(trig ? r.inputs.driverJoy.getDial3() : 0);
        } else if(r.inputs.transportSpin.get()){
            r.cannon.transport(trig ? r.inputs.driverJoy.getDial3() : 0);
        } else if(r.inputs.fireSpin.get()){
            r.cannon.fire(trig ? r.inputs.driverJoy.getDial3() : 0);
        }
    }

    @Override
    public void end(boolean interrupted){
        r.cannon.fire(0);
        r.cannon.transport(0);
        r.intake.intake(0);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
