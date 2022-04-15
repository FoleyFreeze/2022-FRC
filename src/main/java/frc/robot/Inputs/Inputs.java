package frc.robot.Inputs;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
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

    public Trigger getClimbMode = new Trigger(){
        public boolean get(){
            return operatorJoy.climbSwitch();
        }
    };

    public Trigger resetClimberEnc = new Trigger(){
        public boolean get(){
            return driverJoy.resetClimber();
        }
    };

    public Trigger resetNavXAng = new Trigger(){
        public boolean get(){
            return driverJoy.getResetAngle();
        }
    };

    public Trigger resetNavXPos = new Trigger(){
        public boolean get(){
            return driverJoy.resetFieldPos();
        }
    };

    public Trigger resetSwerveAngles = new Trigger(){
        public boolean get(){
            return driverJoy.learnSwerveAngles();
        }
    };

    public boolean cameraDrive(){
        return driverJoy.cameraShoot() && !DriverStation.isAutonomous() || (DriverStation.isAutonomous() && CalsAuton.useCamera);
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

    public Trigger jogUp = new Trigger(){
        public boolean get(){
            return operatorJoy.jogUp();
        }
    };
    
    public Trigger jogDn = new Trigger(){
        public boolean get(){
            return operatorJoy.jogDn();
        }
    };
    
    public Trigger jogLeft = new Trigger(){
        public boolean get(){
            return operatorJoy.jogLeft();
        }
    };
    
    public Trigger jogRight = new Trigger(){
        public boolean get(){
            return operatorJoy.jogRight();
        }
    };

    public Trigger loadCargo = new Trigger(){
        public boolean get(){
            return driverJoy.loadCargo();
        }
    };

    public Trigger gather = new Trigger(){
        public boolean get(){
            return driverJoy.intake();
        }
    };

    public Trigger manualGather = new Trigger(){
        public boolean get(){
            return driverJoy.manualIntake() || operatorJoy.intake() 
                    || operatorJoy.kicker() || operatorJoy.transporter() 
                    || operatorJoy.ejectSwitch();
        }
    };

    public Trigger autoGather = new Trigger(){
        public boolean get(){
            return driverJoy.autoIntake();
        }
    };

    public Trigger intakeSpin = new Trigger(){
        public boolean get(){
            return /*driverJoy.intakeSpin() ||*/ operatorJoy.intake();
        }
    };

    public Trigger transportSpin = new Trigger(){
        public boolean get(){
            return /*driverJoy.transportSpin() ||*/ operatorJoy.transporter();
        }
    };

    public Trigger fireSpin = new Trigger(){
        public boolean get(){
            return /*driverJoy.fireSpin() ||*/ operatorJoy.kicker();
        }
    };

    public Trigger manualClimber = new Trigger(){
        public boolean get(){
            //return driverJoy.manualIntake() && operatorJoy.climbSwitch();
            return (operatorJoy.climbUp() || operatorJoy.climbDn()) && operatorJoy.climbSwitch();
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

    private boolean prevLeftTrig = false;
    public boolean leftTriggerRisingEdge = false;
    public void periodic(){

        boolean leftTrig = driverJoy.intake();
        leftTriggerRisingEdge = leftTrig && !prevLeftTrig;
        prevLeftTrig = leftTrig;

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
            setMaxDrivePower(r.drive.cals.MAX_DRIVE_PWR);
        }

        Log.logBool(hasFlySky, Log.LOG_GROUPS.INPUTS, 5, true, "has FlySky");
        Log.logBool(hasGamePad, Log.LOG_GROUPS.INPUTS, 5, true, "has gamepad");
        SmartDashboard.putNumber("MatchTime", DriverStation.getMatchTime());
    }

    public void setMaxDrivePower(double power){
        if(!r.drive.cals.DISABLED){
            for (Wheel w : r.drive.wheels){
                w.cals.maxPower = power;
            }
        }
    }

    @Override
    public void close() throws Exception {
        driverJoy.close();
        operatorJoy.close();
    }
}