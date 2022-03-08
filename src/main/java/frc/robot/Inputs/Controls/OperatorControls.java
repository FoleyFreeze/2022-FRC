package frc.robot.Inputs.Controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Inputs.CalsCBoard;
import frc.robot.Util.Log;

public class OperatorControls extends Controls implements AutoCloseable{

    public CalsCBoard cals;
    public Joystick controlBoard;

    public OperatorControls(CalsCBoard c){
        cals = c;
    }

    public void detectJoystick(){
        //for each potential joystick port
        for(int i=0; i<DriverStation.kJoystickPorts; i++){
            String name = DriverStation.getJoystickName(i);
            
            if(name.contains("I-PAC") && (controlBoard==null || controlBoard.getPort() != i)) {
                controlBoard = new Joystick(i);
                Log.logString(name, Log.LOG_GROUPS.INPUTS, 1, false, "control board found on port: " + i);
            }
        }
    }

    public boolean climbUp(){
        return checkAxis(cals.climbAxis) > 0.5;
    }

    public boolean climbDn(){
        return checkAxis(cals.climbAxis) < -0.5;
    }

    public boolean shootForward(){
        return checkAxis(cals.shootSwitchAxis) > 0.5;
    }

    public boolean shootBackward(){
        return checkAxis(cals.shootSwitchAxis) < -0.5;
    }

    public boolean climbSwitch(){
        return checkButton(cals.climbSwitch);
    }

    public boolean ejectSwitch(){
        return checkButton(cals.ejectSwitch);
    }

    public boolean hubSwitch(){
        return checkButton(cals.highHubSwitch);
    }

    public boolean shift(){
        return checkButton(cals.shift);
    }

    public boolean fire(){
        return checkButton(cals.shoot);
    }

    public boolean kicker(){
        return checkButton(cals.kicker);
    }

    public boolean transporter(){
        return checkButton(cals.transporter);
    }

    public boolean pitMode(){
        return checkButton(cals.pitMode);
    }

    public boolean intake(){
        return checkButton(cals.intake);
    }

    public boolean checkButton(int button){
        if(controlBoard != null && controlBoard.isConnected()){
            return controlBoard.getRawButton(button);
        }
        return false;
    }

    public double checkAxis(int axis){
        if(controlBoard != null && controlBoard.isConnected()){
            return controlBoard.getRawAxis(axis);
        }
        return 0;
    }

    @Override
    public void close() throws Exception {
        if(controlBoard != null){
            controlBoard = null;
        }
    }
}
