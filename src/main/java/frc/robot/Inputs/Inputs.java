package frc.robot.Inputs;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Inputs.Controls.DriverControls;
import frc.robot.Inputs.Controls.OperatorControls;
import frc.robot.Util.Log;

public class Inputs extends SubsystemBase implements AutoCloseable{

    CalsInputs cals;
    public DriverControls driverJoy;
    public OperatorControls operatorJoy;

    public boolean hasFlySky = false;
    public boolean hasGamePad = false;

    double time;

    public Inputs(CalsInputs inCals, CalsFlysky driverCals, CalsCBoard operatorCals){
        this.cals = inCals;
        driverJoy = new DriverControls(driverCals);
        operatorJoy = new OperatorControls(operatorCals);
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

    public Trigger getFieldOrient = new Trigger(){
        public boolean get(){
            return driverJoy.getFieldOrient();
        }
    };

    public Trigger resetNavXAng = new Trigger(){
        public boolean get(){
            return driverJoy.getResetAngle();
        }
    };

    public Trigger resetNavXPos = new Trigger(){
        public boolean get(){
            return driverJoy.getResetPos();
        }
    };

    public Trigger resetSwerveAngles = new Trigger(){
        public boolean get(){
            return driverJoy.learnSwerveAngles();
        }
    };

    public Trigger primeCannon = new Trigger(){
        public boolean get(){
            return operatorJoy.primeCannon();
        }
    };
    
    public Trigger fireCannon = new Trigger(){
        public boolean get(){
            return operatorJoy.fireCannon();
        }
    };

    public double getDriveX(){
        if(driverJoy != null){
            double val = driverJoy.getX();
            val = deadBand(val, cals.FS_DEADBAND_LOWER, 
                                cals.FS_DEADBAND_UPPER, 
                                cals.FS_INIT_VALUE);
            return expo(val, cals.FS_EXPO);
        } 
        return 0;
    }

    public double getDriveY(){
        if(driverJoy != null){
            double val = driverJoy.getY();
            val = deadBand(val, cals.FS_DEADBAND_LOWER, 
                                  cals.FS_DEADBAND_UPPER, 
                                  cals.FS_INIT_VALUE);
            return -expo(val, cals.FS_EXPO);
        }
        return 0;
    }

    public double getDrivezR(){
        if(driverJoy != null){
            double val = driverJoy.getZ();
            val = deadBand(val, cals.FS_DEADBAND_LOWER, 
                                cals.FS_DEADBAND_UPPER, 
                                cals.FS_INIT_VALUE);
            return -expo(val, cals.FS_EXPO);
        }
        return 0;
    }

    public void periodic(){

        if(Timer.getFPGATimestamp() > time){
            
            driverJoy.detectJoystick();
            operatorJoy.detectJoystick();

            time = Timer.getFPGATimestamp() + cals.CHECK_INTERVAL;
        }
        Log.logBool(hasFlySky, Log.LOG_GROUPS.INPUTS, 5, true, "has FlySky");
        Log.logBool(hasGamePad, Log.LOG_GROUPS.INPUTS, 5, true, "has gamepad");

        Log.logDouble(getDriveY(), Log.LOG_GROUPS.INPUTS, 1, true, "input Y");
        Log.logDouble(getDriveX(), Log.LOG_GROUPS.INPUTS, 1, true, "input X");
    }

    @Override
    public void close() throws Exception {
        
    }
}