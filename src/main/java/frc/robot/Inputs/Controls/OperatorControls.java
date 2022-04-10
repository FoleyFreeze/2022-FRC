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

    boolean hadIPAConPrev = false;
    public void detectJoystick(){
        //for each potential joystick port
        for(int i=0; i<DriverStation.kJoystickPorts; i++){
            String name = DriverStation.getJoystickName(i);
            
            boolean hasIPAC = name.contains("I-PAC");
            if(hasIPAC && !hadIPAConPrev && (controlBoard==null /*|| controlBoard.getPort() != i*/)) {
                controlBoard = new Joystick(i);
                Log.logString(name, Log.LOG_GROUPS.INPUTS, 1, false, "control board found on port: " + i);
            }
            hadIPAConPrev = hasIPAC;
        }
    }


    //climb buttons
    public boolean climbUp(){
        return checkAxis(cals.climbAxis) > 0.5;
    }

    public boolean climbDn(){
        return checkAxis(cals.climbAxis) < -0.5;
    }


    //shooter buttons
    public boolean layUpShot(){
        return checkAxis(cals.distAxis) < -0.5;
    }
    
    public boolean launchPadShot(){
        return checkAxis(cals.distAxis) > 0.5;
    }

    public boolean shootForward(){
        return checkButton(cals.shootBackward);
    }

    public boolean shootBackward(){
        return !checkButton(cals.shootBackward);
    }

    public boolean climbSwitch(){
        return checkButton(cals.climbSwitch);
    }

    public boolean hubSwitch(){
        return !checkButton(cals.highHubSwitch);
    }

    public boolean fire(){
        return checkButton(cals.shoot);
    }

    public boolean kicker(){
        return checkButton(cals.kicker);
    }

    public boolean jogUp(){
        return checkPOV() == 0;
    }

    public boolean jogDn(){
        return checkPOV() == 180;
    }

    public boolean jogLeft(){
        return checkPOV() == 270;
    }

    public boolean jogRight(){
        return checkPOV() == 90;
    }


    //intake buttons
    public boolean intake(){
        return checkButton(cals.intake);
    }

    public boolean ejectSwitch(){
        return checkButton(cals.ejectSwitch);
    }

    public boolean transporter(){
        return checkButton(cals.transporter);
    }
    

    //misc buttons
    public boolean shift(){
        return checkButton(cals.shift);
    }

    public boolean pitMode(){
        //field mode is true, so invert this, but also force it false when the FMS is active
        //also use button inverse which forces it to true when the control board is not plugged in
        return !checkButtonInverse(cals.pitMode) && !DriverStation.isFMSAttached();
    }

    

    public boolean checkButton(int button){
        if(controlBoard != null && controlBoard.isConnected()){
            return controlBoard.getRawButton(button);
        }
        return false;
    }

    public boolean checkButtonInverse(int button){
        if(controlBoard != null && controlBoard.isConnected()){
            return controlBoard.getRawButton(button);
        }
        return true;
    }

    public double checkAxis(int axis){
        if(controlBoard != null && controlBoard.isConnected()){
            return controlBoard.getRawAxis(axis);
        }
        return 0;
    }

    public int checkPOV(){
        if(controlBoard != null && controlBoard.isConnected()){
            return controlBoard.getPOV();
        }
        return -1;
    }

    @Override
    public void close() throws Exception {
        if(controlBoard != null){
            controlBoard = null;
        }
    }
}
