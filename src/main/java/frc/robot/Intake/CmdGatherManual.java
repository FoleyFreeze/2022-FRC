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
        boolean run = r.inputs.gather.get();
        double value = r.inputs.driverJoy.getDial3();

        //if the command is coming from the control board use a default value
        if(!r.inputs.driverJoy.manualIntake()) {
            run = true;
            value = 0.5;
        }
        
        //if shift is pressed, run backwards
        if(r.inputs.operatorJoy.shift()) value = -value;

        if(r.inputs.operatorJoy.ejectSwitch() && r.inputs.operatorJoy.shift()){
            r.cannon.prime(500, 55, false);
            r.cannon.fire(1);
        } else if(r.inputs.operatorJoy.ejectSwitch()){
            r.intake.intake(-0.5);
            r.cannon.transport(-0.5);
        } else if(r.inputs.intakeSpin.get()){
            r.intake.intake(run ? value : 0);
        } else if(r.inputs.fireSpin.get()){
            r.cannon.fire(run ? value : 0);
        } else if(r.inputs.transportSpin.get()){
            r.cannon.transport(run ? value : 0);
        }
    }

    @Override
    public void end(boolean interrupted){
        r.cannon.fire(0);
        r.cannon.transport(0);
        r.intake.intake(0);
        r.cannon.prime(0, 55, false);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
