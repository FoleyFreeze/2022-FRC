package frc.robot.Inputs;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Log;

public class Inputs extends SubsystemBase{

    CalsInputs cals;
    public Joystick controller;

    public boolean hasFlySky = false;
    public boolean hasGamePad = false;

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

            Log.logBool(hasFlySky, Log.LOG_GROUPS.INPUTS, 1, false, "has FlySky");
            Log.logBool(hasGamePad, Log.LOG_GROUPS.INPUTS, 1, false, "has gamepad");
            Log.logString(name, Log.LOG_GROUPS.INPUTS, 1, false, "name");
        }
    }
}