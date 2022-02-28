package frc.robot.Inputs.Controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Inputs.CalsFlysky;
import frc.robot.Inputs.CalsGamepad;
import frc.robot.Util.Log;

public class DriverControls extends Controls implements AutoCloseable{
    public CalsFlysky activeCals;
    public CalsFlysky fsCals;
    public CalsGamepad gPadCals;

    public Joystick joystick;

    public boolean isFlySky = false;
    public boolean isGamePad = false;

    public void detectJoystick(){
        //for each potential joystick port
        if(!isFlySky || !joystick.isConnected()){
            for(int i=0; i<DriverStation.kJoystickPorts; i++){
                String name = DriverStation.getJoystickName(i);

                if(name.contains("flysky") || joystick==null || joystick.getPort() != i) {
                    isFlySky = true;
                    isGamePad = false;
                    activeCals = fsCals;
                    joystick = new Joystick(i);
                    Log.logString(name, Log.LOG_GROUPS.INPUTS, 1, false, "flysky found on port: " + i);
                    return;//if we found the flysky, we are done checking
                }
                if(name.contains("gamepad") || joystick==null || joystick.getPort() != i) {
                    isFlySky = false;
                    isGamePad = true;
                    activeCals = gPadCals;
                    joystick = new Joystick(i);
                    Log.logString(name, Log.LOG_GROUPS.INPUTS, 1, false, "gamepad found on port: " + i);
                }
            }
        }
    }

    public DriverControls(CalsFlysky c){
        activeCals = c;
    }


    //drive joystick buttons
    public double getX(){
        return joystick.getRawAxis(activeCals.X_AXIS);
    }

    public double getY(){
        return joystick.getRawAxis(activeCals.Y_AXIS);
    }

    public double getZ(){
        return joystick.getRawAxis(activeCals.Z_AXIS);
    }

    public boolean getFieldOrient(){
        return checkButtons(joystick, activeCals.fieldOrient);
    }

    public boolean getResetAngle(){
        return checkButtons(joystick, activeCals.resetAngle);
    }

    public boolean getResetPos(){
        return checkButtons(joystick, activeCals.resetPosition);
    }

    double timer;
    public boolean learnSwerveAngles(){
        if(checkButtons(joystick, activeCals.learnWheelPositions)){
            return Timer.getFPGATimestamp() - activeCals.wheelLearnWaitTime > timer;
        } else {
            timer = Timer.getFPGATimestamp();
            return false;
        }
    }

    //cannon joystick buttons
    public boolean fireCannon(){
        if(joystick != null){
            return joystick.getRawAxis(activeCals.fireCannon) > 0.5;
        }else{
            return false;
        }
    }

    public boolean cameraShoot(){
        return checkButtons(joystick, activeCals.cameraShoot);
    }

    public boolean loadCargo(){
        return checkButtons(joystick, activeCals.loadCargo);
    }

    public boolean resetCannonAng(){
        return checkButtons(joystick, activeCals.resetCannon);
    }

    public boolean intake(){
        return checkButtons(joystick, activeCals.intake);
    }

    @Override
    public void close() throws Exception {
        if(joystick != null){
            joystick = null;
        }
    }
}
