package frc.robot.Inputs;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.Drive.CalsDrive;
import frc.robot.Drive.Wheel;
import frc.robot.Inputs.Controls.DriverControls;
import frc.robot.Inputs.Controls.OperatorControls;
import frc.robot.Util.Log;
import frc.robot.Util.Vector;

public class Inputs extends SubsystemBase implements AutoCloseable{

    RobotContainer r;
    CalsInputs cals;
    public DriverControls driverJoy;
    public OperatorControls operatorJoy;

    public boolean hasFlySky = false;
    public boolean hasGamePad = false;

    double time;

    public Inputs(CalsInputs inCals, CalsFlysky driverCals, CalsCBoard operatorCals, RobotContainer r){
        this.r = r;
        this.cals = inCals;
        driverJoy = new DriverControls();
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

    public static void mapSquareToCircle(Vector v){
        double pi = Math.PI;
        double maxMag;

        if(v.theta < 3*pi/4 && v.theta > pi/4
                || v.theta < -pi/4 && v.theta > -3*pi/4){
            maxMag = Math.abs(1 / Math.sin(v.theta));
        } else {
            maxMag = Math.abs(1 / Math.cos(v.theta));
        }

        v.r /= maxMag;
    }

    public boolean getFieldOrient(){
        return driverJoy.getFieldOrient() && !DriverStation.isAutonomousEnabled();
    }

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

    public boolean cameraDrive(){
        return driverJoy.cameraShoot();
    }
    
    public Trigger fireCannon = new Trigger(){
        public boolean get(){
            return driverJoy.fireCannon();
        }
    };

    public Trigger resetCannon = new Trigger(){
        public boolean get(){
            return driverJoy.resetCannonAng();
        }
    };

    public Trigger sensorResetCannon = new Trigger(){
        public boolean get(){
            return driverJoy.getSensorCannonReset() && r.sensors.cannonAngleSensor.get();
        }
    };

    public Trigger loadCargo = new Trigger(){
        public boolean get(){
            return driverJoy.loadCargo();
        }
    };

    public Trigger gather = new Trigger(){
        public boolean get(){
            return driverJoy.intake() || operatorJoy.intake() 
                    || operatorJoy.kicker() || operatorJoy.fire();
        }
    };

    public Trigger manualGather = new Trigger(){
        public boolean get(){
            return driverJoy.manualIntake() || operatorJoy.intake() 
                    || operatorJoy.kicker() || operatorJoy.fire() || operatorJoy.ejectSwitch();
        }
    };

    public Trigger autoGather = new Trigger(){
        public boolean get(){
            return driverJoy.autoIntake();
        }
    };

    public Trigger intakeSpin = new Trigger(){
        public boolean get(){
            return driverJoy.intakeSpin() || operatorJoy.intake();
        }
    };

    public Trigger transportSpin = new Trigger(){
        public boolean get(){
            return driverJoy.transportSpin() || operatorJoy.transporter();
        }
    };

    public Trigger fireSpin = new Trigger(){
        public boolean get(){
            return driverJoy.fireSpin() || operatorJoy.fire();
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

        if(operatorJoy.pitMode()) {
            setMaxDrivePower(r.drive.cals.MAX_PIT_PWR);
        } else if(operatorJoy.climbSwitch()) {
            setMaxDrivePower(r.drive.cals.maxDrivePowerClimb);
        } else {
            setMaxDrivePower(CalsDrive.MAX_DRIVE_PWR);
        }

        Log.logBool(hasFlySky, Log.LOG_GROUPS.INPUTS, 5, true, "has FlySky");
        Log.logBool(hasGamePad, Log.LOG_GROUPS.INPUTS, 5, true, "has gamepad");

        Log.logDouble(getDriveY(), Log.LOG_GROUPS.INPUTS, 1, true, "input Y");
        Log.logDouble(getDriveX(), Log.LOG_GROUPS.INPUTS, 1, true, "input X");
    }

    public void setMaxDrivePower(double power){
        for (Wheel w : r.drive.wheels){
            w.cals.maxPower = power;
        }
    }

    @Override
    public void close() throws Exception {
        driverJoy.close();
        operatorJoy.close();
    }
}