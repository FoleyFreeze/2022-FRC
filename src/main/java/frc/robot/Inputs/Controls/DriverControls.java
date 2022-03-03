package frc.robot.Inputs.Controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Inputs.CalsFlysky;
import frc.robot.Inputs.CalsGamepad;
import frc.robot.Util.Log;

public class DriverControls extends Controls implements AutoCloseable{
    public CalsFlysky fsCals = new CalsFlysky();
    public CalsGamepad gPadCals = new CalsGamepad();
    public CalsFlysky activeCals = fsCals;
    

    public Joystick joystick;

    public boolean isFlySky = false;
    public boolean isGamePad = false;

    public void detectJoystick(){
        //for each potential joystick port
        if(!isFlySky || !joystick.isConnected()){
            /*
            isFlySky = true;
            joystick = new Joystick(0);
            */
            
            for(int i=0; i<DriverStation.kJoystickPorts; i++){
                String name = DriverStation.getJoystickName(i);

                if(name.contains("FlySky") && (joystick==null || joystick.getPort() != i)) {
                    isFlySky = true;
                    isGamePad = false;
                    activeCals = fsCals;
                    joystick = new Joystick(i);
                    Log.logString(name, Log.LOG_GROUPS.INPUTS, 1, false, "flysky found on port: " + i);
                    return;//if we found the flysky, we are done checking
                }
                if(name.contains("gamepad") && (joystick==null || joystick.getPort() != i)) {
                    isFlySky = false;
                    isGamePad = true;
                    activeCals = gPadCals;
                    joystick = new Joystick(i);
                    Log.logString(name, Log.LOG_GROUPS.INPUTS, 1, false, "gamepad found on port: " + i);
                }
            }
        }
    }

    public DriverControls(){
        //activeCals = c;
    }


    //drive joystick buttons
    public double getX(){
        return checkAxis(joystick, activeCals.X_AXIS);
    }

    public double getY(){
        return checkAxis(joystick, activeCals.Y_AXIS);
    }

    public double getZ(){
        return checkAxis(joystick, activeCals.Z_AXIS);
    }

    double dial1Val;
    public double getDial1(){
        if(checkButtons(joystick, activeCals.BOT_RIGHT_3POS_UP)){
            dial1Val = (checkAxis(joystick, activeCals.RIGHT_DIAL) + 1) / 2.0;
        }
        return dial1Val;
    }

    double dial2Val;
    public double getDial2(){
        if(checkButtons(joystick, activeCals.BOT_RIGHT_3POS_DOWN)){
            dial2Val = (checkAxis(joystick, activeCals.RIGHT_DIAL) + 1) / 2.0;
        }
        return dial2Val;
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
        return checkAxis(joystick, activeCals.intake) > 0.5;
    }

    @Override
    public void close() throws Exception {
        if(joystick != null){
            joystick = null;
        }
    }
}
