package frc.robot.Inputs.Controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Controls {
    //finding button index value

    private int flags;
    private int getButtonIndex(){
        int i = 1;
        int idx = 1; //index starts counting at 1, not 0
        while((flags & i) == 0 && i < (1 << 16)){
            i = i << 1;
            idx++;
        }

        flags &= ~i;
        return idx;
    }

    public boolean checkButtons(Joystick j, int f){
        if(f == 0 || j == null || !j.isConnected()) return false;

        flags = f;
        boolean output = true;

        int count = 0;
        while(flags > 0 && count < 16){
            int idx = getButtonIndex();
            output &= j.getRawButton(idx);
            count++;
        }

        return output;
    }

    public double checkAxis(Joystick j, int axis){
        if(j!=null){
            return j.getRawAxis(axis);
        } else {
            return 0;
        }
    }
}
