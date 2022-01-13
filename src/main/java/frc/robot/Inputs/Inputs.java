package frc.robot.Inputs;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Util;

public class Inputs extends SubsystemBase{

    CalsInputs cals;
    public Joystick controller;

    public boolean hasFlySky;
    public boolean hasGamePad;

    double time;

    public Inputs(CalsInputs cals){
        this.cals = cals;

        controller = new Joystick(cals.JOYPORT);
    }

    public boolean isGamePieceDetected(){
        return false;
    }

    public void periodic(){
        //joystick detection

        String name = "null";

        if(Timer.getFPGATimestamp() > time){
            if(controller.isConnected()){
                name = controller.getName();

                if(name.contains("FlySky")){
                    hasFlySky = true;
                    name = "FlySky";
                } else{
                    hasGamePad = true;
                    name = "GamePad";
                }
            } else{
                hasFlySky = false;
                hasGamePad = false;
            }

            time = Timer.getFPGATimestamp() + cals.CHECK_INTERVAL;

            Util.logBool(hasFlySky, Util.LOG_GROUPS.INPUTS, 1, false, "has FlySky");
            Util.logBool(hasGamePad, Util.LOG_GROUPS.INPUTS, 1, false, "has gamepad");
            Util.logString(name, Util.LOG_GROUPS.INPUTS, 1, false, "name");
        }
    }
}