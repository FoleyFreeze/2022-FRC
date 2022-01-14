package frc.robot.Intake;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class CmdGather extends CommandBase{
    
    SysIntake m_subsystem;

    Spark intakeMotor;

    CalsIntake cals;

    public CmdGather(CalsIntake cals){
        this.cals = cals;

        intakeMotor = new Spark(cals.INTAKE_MOTOR_CHANNEL);
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
