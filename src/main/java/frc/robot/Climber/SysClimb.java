package frc.robot.Climber;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Util.SmartDigitalInput;
import frc.robot.Util.Motor.Motor;

public class SysClimb extends SubsystemBase implements AutoCloseable{

    RobotContainer r;
    public CalsClimb cals;

    public Motor climbArmL;
    public Motor climbArmR;
    public Motor climbWinch;

    public SmartDigitalInput limSwitchL;
    public SmartDigitalInput limSwitchR;

    public SysClimb(CalsClimb cals, RobotContainer r){
        this.r = r;
        this.cals = cals;
        if (cals.DISABLED) return;

        climbArmL = Motor.create(cals.climbArmL);
        climbArmR = Motor.create(cals.climbArmR);
        climbWinch = Motor.create(cals.climbWinch);

        limSwitchL = new SmartDigitalInput(18);
        limSwitchR = new SmartDigitalInput(19);
        limSwitchL.invert();
        limSwitchR.invert();
    }

    public void resetEncoders(){
        climbArmL.resetEncoder();
        climbArmR.resetEncoder();
        climbWinch.resetEncoder();
    }

    public void driveArms(double pwr){
        climbArmL.setPower(pwr);
        climbArmR.setPower(pwr);
    }

    public void driveArms(double left, double right){
        climbArmL.setPower(left);
        climbArmR.setPower(right);
    }

    public void driveArms(){
        driveArms(cals.armPower);
    }

    public void releaseArms(){
        driveArms(cals.releaseArmsPower);
    }

    public void driveWinch(double pwr){
        climbWinch.setPower(pwr);
    }

    public void driveWinch(){
        driveWinch(cals.winchPower.get());
    }

    public void releaseWinch(){
        driveWinch(cals.releaseWinchPower);
    }

    boolean prevState = false;
    @Override
    public void periodic(){
        if (cals.DISABLED) return;

        //reset climb stage upon falling edge of climb switch
        boolean currState = r.inputs.getClimbMode.get();
        CmdClimb.resetStage(!currState && prevState);
        prevState = currState;

        SmartDashboard.putNumber("Left Arm", climbArmL.getPosition()*360);
        SmartDashboard.putNumber("Right Arm", climbArmR.getPosition()*360);
        SmartDashboard.putNumber("Winch Pos", climbWinch.getPosition());
        SmartDashboard.putNumber("DialPower", r.inputs.driverJoy.getDial3());
        SmartDashboard.putBoolean("L climb lim sw", r.climb.limSwitchL.get());
        SmartDashboard.putBoolean("R climb lim sw", r.climb.limSwitchR.get());
    }

    @Override
    public void close() throws Exception {
        climbArmL.close();
        climbWinch.close();
    }
}
