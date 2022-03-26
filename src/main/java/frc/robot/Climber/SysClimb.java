package frc.robot.Climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Util.Motor.Motor;

public class SysClimb extends SubsystemBase implements AutoCloseable{

    RobotContainer r;
    public CalsClimb cals;

    public Motor climbArms;
    public Motor climbWinch;

    public SysClimb(CalsClimb cals, RobotContainer r){
        this.r = r;
        this.cals = cals;
        if (cals.DISABLED) return;

        climbArms = Motor.create(cals.climbArms);
        climbWinch = Motor.create(cals.climbWinch);
    }

    public void driveArms(double pwr){
        climbArms.setPower(pwr);
    }

    public void driveArms(){
        driveArms(cals.armPower);
    }

    public void driveWinch(double pwr){
        climbWinch.setPower(pwr);
    }

    public void driveWinch(){
        driveArms(cals.winchPower);
    }

    boolean prevState = false;
    @Override
    public void periodic(){
        if (cals.DISABLED) return;

        //reset climb stage upon falling edge of climb switch
        boolean currState = r.inputs.getClimbMode.get();
        CmdClimb.resetStage(!currState && prevState);
        prevState = currState;
    }

    @Override
    public void close() throws Exception {
        climbArms.close();
        climbWinch.close();
    }
}
