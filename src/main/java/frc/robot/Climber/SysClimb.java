package frc.robot.Climber;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Util.Log;
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

    public SmartDigitalInput barSensorL;
    public SmartDigitalInput barSensorR;

    static enum CLIMB_STAGE{
        RESET, ARMS, WINCH, HOOK, DONE
    }

    public SysClimb(CalsClimb cals, RobotContainer r){
        this.r = r;
        this.cals = cals;
        if (cals.DISABLED) return;

        climbArmL = Motor.create(cals.climbArmL);
        climbArmR = Motor.create(cals.climbArmR);
        climbWinch = Motor.create(cals.climbWinch);

        //limSwitchL = new SmartDigitalInput(6);
        //limSwitchR = new SmartDigitalInput(7);

        //barSensorL = new SmartDigitalInput(0);
        //barSensorR = new SmartDigitalInput(0);
    }

    public void resetEncoders(){
        climbArmL.resetEncoder();
        climbArmR.resetEncoder();
        //climbWinch.resetEncoder();
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
    boolean manualClimb = false;
    boolean manualClimbArms = false;
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
        //SmartDashboard.putNumber("DialPower", r.inputs.driverJoy.getDial3());
        //SmartDashboard.putBoolean("L climb lim sw", r.climb.limSwitchL.get());
        //SmartDashboard.putBoolean("R climb lim sw", r.climb.limSwitchR.get());

        //manual winch climb control
        if(r.inputs.operatorJoy.climbUp() && r.inputs.operatorJoy.shift()){
            manualClimb = true;
            driveWinch(0.25);
        } else if(r.inputs.operatorJoy.climbDn() && r.inputs.operatorJoy.shift()){
            manualClimb = true;
            driveWinch(-0.25);
        } else if(manualClimb){
            manualClimb = false;
            driveWinch(0);
        }

        //manual arms climb control
        if(r.inputs.operatorJoy.climbUp() && !r.inputs.operatorJoy.shift()){
            manualClimbArms = true;
            driveArms(0.2);
        } else if(r.inputs.operatorJoy.climbDn() && !r.inputs.operatorJoy.shift()){
            manualClimbArms = true;
            driveArms(-0.2);
        } else {
            manualClimbArms = false;
            driveArms(0);
        }

        SmartDashboard.putNumber("ClimbCurr",climbWinch.getMotorSideCurrent());

        Log.addValue(getStage(CmdClimb.stage).toString(), "climb stage", Log.compTab);
    }

    @Override
    public void close() throws Exception {
        climbArmL.close();
        climbArmR.close();
        climbWinch.close();
    }

    private CLIMB_STAGE getStage(int stage){
        if(stage == 0){
            return CLIMB_STAGE.RESET;
        } else if(stage == 1 || stage == 4 || stage == 7){
            return CLIMB_STAGE.ARMS;
        } else if(stage == 2 || stage == 5 || stage == 8){
            return CLIMB_STAGE.WINCH;
        } else if(stage == 3 || stage == 6 || stage == 9){
            return CLIMB_STAGE.HOOK;
        } else {
            return CLIMB_STAGE.DONE;
        }
    }
}
