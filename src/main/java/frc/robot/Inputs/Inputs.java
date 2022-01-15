package frc.robot.Inputs;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Log;

public class Inputs extends SubsystemBase{

    CalsInputs cals;
    public Joystick flysky;

    public boolean hasFlySky = false;
    public boolean hasGamePad = false;

    double time;

    public Inputs(CalsInputs cals){
        this.cals = cals;

        //controller = new Joystick(cals.JOYPORT);
        
    }

    public double getDriveX(){
        if(flysky != null){
            return flysky.getRawAxis(0);
        } 
        return 0;
    }

    public double getDriveY(){
        if(flysky != null){
            return flysky.getRawAxis(1);
        }
        return 0;
    }

    public double getDrivezR(){
        if(flysky != null){
            return flysky.getRawAxis(4);
        }
        return 0;
    }

    public void periodic(){

        if(Timer.getFPGATimestamp() > time){

            //for each potential joystick port
            for(int i=0; i<DriverStation.kJoystickPorts; i++){
                String name = DriverStation.getJoystickName(i);
                
                if(name.contains("FlySky") && (flysky==null || flysky.getPort() != i)) {
                    flysky = new Joystick(i);
                    Log.logString(name, Log.LOG_GROUPS.INPUTS, 1, false, "Flysky found on port: " + i);
                }
            }

            time = Timer.getFPGATimestamp() + cals.CHECK_INTERVAL;

            //Log.logBool(hasFlySky, Log.LOG_GROUPS.INPUTS, 5, false, "has FlySky");
            //Log.logBool(hasGamePad, Log.LOG_GROUPS.INPUTS, 5, false, "has gamepad");
            //Log.logString(name, Log.LOG_GROUPS.INPUTS, 5, false, "name");
            
        }
    }
}