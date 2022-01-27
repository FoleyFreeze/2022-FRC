package frc.robot.Inputs;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Util.Log;

public class Inputs extends SubsystemBase implements AutoCloseable{

    CalsInputs cals;
    public Joystick flysky = null;

    public boolean hasFlySky = false;
    public boolean hasGamePad = false;

    double time;

    public Trigger resetSwerveAngles = new Trigger(){
        public boolean get(){
            if(flysky != null){
                boolean b = flysky.getRawButton(10) && flysky.getRawButton(14);
                return b;
            } else {
                return false;
            }
        }
    };

    public Inputs(CalsInputs cals){
        this.cals = cals;
        flysky = new Joystick(0);
    }

    public double expo(double value, double exponent){
        return Math.pow(Math.abs(value), exponent) * Math.signum(value);
    }

    public double deadBand(double value, double lo, double hi, double min){
        double valSign = Math.signum(value);
        double absVal = Math.abs(value);

        if(absVal < lo){
            return 0;
        }else if(absVal > hi){
            return 1.0 * valSign;
        }else{
            //mapping input range to output range
            double inputRange = hi - lo;
            double outputRange = 1 - min;
            return (absVal - lo) * valSign / inputRange * outputRange + min;
        }
    }

    public boolean getFieldOrient(){
        if(flysky != null){
            return flysky.getRawButton(cals.FLYSKY_FIELD_ORIENT);
        }
        return false;
    }

    public double getDriveX(){
        if(flysky != null){
            double val = deadBand(flysky.getRawAxis(0), 
                                  cals.FS_DEADBAND_LOWER, 
                                  cals.FS_DEADBAND_UPPER, 
                                  cals.FS_INIT_VALUE);
            return expo(val, cals.FS_EXPO);
        } 
        return 0;
    }

    public double getDriveY(){
        if(flysky != null){
            double val = deadBand(flysky.getRawAxis(1), 
                                  cals.FS_DEADBAND_LOWER, 
                                  cals.FS_DEADBAND_UPPER, 
                                  cals.FS_INIT_VALUE);
            return -expo(val, cals.FS_EXPO);
        }
        return 0;
    }

    public double getDrivezR(){
        if(flysky != null){
            double val = deadBand(flysky.getRawAxis(4), 
                                  cals.FS_DEADBAND_LOWER, 
                                  cals.FS_DEADBAND_UPPER, 
                                  cals.FS_INIT_VALUE);
            return -expo(val, cals.FS_EXPO);
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

            Log.logBool(hasFlySky, Log.LOG_GROUPS.INPUTS, 5, true, "has FlySky");
            Log.logBool(hasGamePad, Log.LOG_GROUPS.INPUTS, 5, true, "has gamepad");
            Log.logString(DriverStation.getJoystickName(0), Log.LOG_GROUPS.INPUTS, 5, false, "name");

        }

        Log.logDouble(getDriveY(), Log.LOG_GROUPS.INPUTS, 1, true, "input Y");
        Log.logDouble(getDriveX(), Log.LOG_GROUPS.INPUTS, 1, true, "input X");
    }

    @Override
    public void close() throws Exception {
        
    }
}