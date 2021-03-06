package frc.robot.Inputs.Controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
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



    //drive
    public double getX(){
        return checkAxis(joystick, activeCals.X_AXIS);
    }

    public double getY(){
        return checkAxis(joystick, activeCals.Y_AXIS);
    }

    public double getZ(){
        return checkAxis(joystick, activeCals.Z_AXIS);
    }

    public boolean getFieldOrient(){
        return checkButtons(joystick, activeCals.fieldOrient);
    }

    public boolean resetFieldPos(){
        return checkButtons(joystick, activeCals.resetPosition);
    }

    double swerveAngTimer;
    public boolean learnSwerveAngles(){
        if(checkButtons(joystick, activeCals.learnWheelPositions)){
            return Timer.getFPGATimestamp() - activeCals.wheelLearnWaitTime > swerveAngTimer;
        } else {
            swerveAngTimer = Timer.getFPGATimestamp();
            return false;
        }
    }

    double climbResetTime;
    public boolean resetClimber(){
        if(checkButtons(joystick, activeCals.resetClimbEncoder)){
            return Timer.getFPGATimestamp() - activeCals.climbResetWaitTime > climbResetTime;
        } else {
            climbResetTime = Timer.getFPGATimestamp();
            return false;
        }
    }

    //cannon
    public boolean getResetAngle(){
        return checkButtons(joystick, activeCals.resetAngle);
    }

    double cannonResetTimer;
    public boolean getSensorCannonReset(){
        if(checkButtons(joystick, activeCals.sensorResetCannon)){
            return Timer.getFPGATimestamp() - activeCals.wheelLearnWaitTime > cannonResetTimer;
        } else {
            cannonResetTimer = Timer.getFPGATimestamp();
            return false;
        }
    }

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

    public boolean resetCannonAng(){
        return checkButtons(joystick, activeCals.resetCannon);
    }

    public boolean fireSpin(){
        return checkButtons(joystick, activeCals.fireSpin);
    }

    /*public boolean layUpShot(){
        return checkButtons(joystick, activeCals.layupShot);
    }*/
    
    /*public boolean launchPadShot(){
        return checkButtons(joystick, activeCals.launchPadShot);
    }*/



    //intake buttons
    public boolean loadCargo(){
        return checkButtons(joystick, activeCals.loadCargo);
    }

    public boolean intakeAxis(){
        return checkAxis(joystick, activeCals.intake) > 0.5;
    }

    public boolean intake(){
        return intakeAxis();
    }

    public boolean manualIntake(){
        return checkButtons(joystick, activeCals.manualIntake);
    }

    public boolean autoIntake(){
        return checkButtons(joystick, activeCals.TOP_LEFT_3POS_UP);
    }

    public boolean intakeSpin(){
        return checkButtons(joystick, activeCals.intakeSpin);
    }
    
    public boolean transportSpin(){
        return !intakeSpin() && !fireSpin();
    }



    //misc
    double dial1Val = 0.154;
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

    public double getDial3(){
        return checkAxis(joystick, activeCals.RIGHT_DIAL);
    }

    

    @Override
    public void close() throws Exception {
        if(joystick != null){
            joystick = null;
        }
    }
}
