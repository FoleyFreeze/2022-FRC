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
            
            if(controlBoard.getName().contains("foley") || controlBoard==null || controlBoard.getPort() != i) {
                controlBoard = new Joystick(i);
                Log.logString(name, Log.LOG_GROUPS.INPUTS, 1, false, "control board found on port: " + i);
            }
        }
    }


    //cannon joystick buttons
    public boolean primeCannon(){
        return checkButtons(controlBoard, cals.prime);
    }

    public boolean fireCannon(){
        return checkButtons(controlBoard, cals.fire);
    }

    @Override
    public void close() throws Exception {
        if(controlBoard != null){
            controlBoard = null;
        }
    }
}
