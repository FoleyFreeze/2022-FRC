package frc.robot.Inputs.Controls;

import edu.wpi.first.wpilibj.Joystick;

public class Controls {
    //finding button index value

    private int flags;
    private int getButtonIndex(){
        int i = 1;
        int idx = 1; //index starts counting at 1, not 0
        while((flags & i) != 0){
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

        while(flags > 0){
            int idx = getButtonIndex();
            output &= j.getRawButton(idx);
        }

        return output;
    }
}
