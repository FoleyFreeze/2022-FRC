package frc.robot.Cannon;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
import frc.robot.Util.Angle;
import frc.robot.Util.Interpolate;
import frc.robot.Util.Log;
import frc.robot.Util.Vector;
import frc.robot.Util.Motor.Motor;

public class SysCannon extends SubsystemBase implements AutoCloseable{
    
    public CalsCannon cals;
    RobotContainer r;
    
    Motor cwMotor;
    Motor ccwMotor;
    public Motor angleMotor;
    Motor leftFireMotor;
    Motor rightFireMotor;
    Motor transpMotor;

    AnalogInput anglePot;

    double jogSpeed;
    double jogAng;

    boolean first;
    boolean potDisabled = false;

    public SysCannon(CalsCannon cals, RobotContainer r){
        this.cals = cals;
        if (cals.DISABLED) return;
        this.r = r;

        jogSpeed = cals.jogInitSpeed;
        jogAng = cals.jogInitAng;
        
        cwMotor = Motor.create(cals.cwMotor);
        ccwMotor = Motor.create(cals.ccwMotor);
        angleMotor = Motor.create(cals.angleMotor);
        leftFireMotor = Motor.create(cals.leftFireMotor);
        rightFireMotor = Motor.create(cals.rightFireMotor);
        transpMotor = Motor.create(cals.transpMotor);

        anglePot = new AnalogInput(7);
        
        first = true;
    }

    public void prime(){
        prime(true);
    }

    public void prime(boolean setAngle){
        if (cals.DISABLED) return;
        if(r.inputs.driverJoy.layUpShot() && !DriverStation.isAutonomous()){//layup
            prime(cals.LAYUP_SHOOT_SPEED, flip(cals.LAYUP_SHOOT_ANG), setAngle);
        }else if(cals.useDistanceLookup.get() != 0 && r.inputs.cameraDrive() && r.sensors.hasTargetImage()){//using dist look-up table
            Vector v = Vector.subVectors(r.sensors.target.location, r.sensors.botLoc);
            SmartDashboard.putNumber("PrimeDist", v.r);
            prime(v.r, setAngle);
        } else if(DriverStation.isAutonomousEnabled()){//auton shot
            //prime(cals.TARMAC_SHOOT_SPEED, flip(cals.TARMAC_SHOOT_ANG), setAngle);
            prime(CalsAuton.autonDist, setAngle);
        } else if(cals.useVariableShootSpeed){//dial speeds
            double speed = r.inputs.driverJoy.getDial1() * 
                    (cals.maxVariableShootSpeed - cals.minVariableShootSpeed)
                     + cals.minVariableShootSpeed;
            double angle = r.inputs.driverJoy.getDial2() *
                    (cals.shootMaxAngle - cals.shootMinAngle)
                    + cals.shootMinAngle;
            prime(speed, angle, setAngle);
        } else if(!r.inputs.operatorJoy.hubSwitch()){//low shot selected
            prime(cals.LOW_SHOOT_SPEED, flip(cals.LOW_SHOOT_ANG), setAngle);
        }else if(r.inputs.driverJoy.launchPadShot()){//launch pad
            prime(cals.LAUNCH_PAD_SHOOT_SPEED, flip(cals.LAUNCH_PAD_SHOOT_ANG), setAngle);
        } else{//joe shot
            prime(cals.TARMAC_SHOOT_SPEED.get(), flip(cals.TARMAC_SHOOT_ANG.get()), setAngle);
        }
    }

    public void prime(double dist, boolean spinIt){
        if (cals.DISABLED) return;
        
        double speed = 0;
        if(spinIt) {
            speed = Interpolate.interpolate(cals.distances, cals.speeds, dist);
        }
        double angle = Interpolate.interpolate(cals.distances, cals.angles, dist);

        SmartDashboard.putString("ShooterSetpoint", String.format("%.0f, %.0f", speed, angle));

        prime(speed, angle);
    }

    public void prime(double speed, double angle){
        prime(speed, angle, true);
    }

    public void prime(double speed, double angle, boolean setAngle){
        if (cals.DISABLED) return;

        if(speed == 0){
            setPower(0, 0);
        } else {
            setSpeed(speed + jogSpeed, speed + jogSpeed);
        }

        if(setAngle) setAngle(angle + jogAng - cals.angOffset);
        
    }

    double flip(double ang){
        if(r.inputs.operatorJoy.shootForward()){
            return 180 - ang;//point shooter the other way
        }
        return ang;
    }

    public void fire(double power){
        if (cals.DISABLED) return;
        leftFireMotor.setPower(power);
        rightFireMotor.setPower(power);
    }

    public void fire(){
        if (cals.DISABLED) return;
        fire(cals.wheelOfFirePower);
    }

    double timeAngleWasSet;
    public void setAngle(double angle){
        if (cals.DISABLED) return;
        
        if (angle > cals.shootMaxAngle)
            angle = cals.shootMaxAngle;

        if (angle < cals.shootMinAngle)
            angle = cals.shootMinAngle;

        double revs = angle / 360;
        angleMotor.setPosition(revs);
    }

    public void resetTimeAngleWasSet(){
        timeAngleWasSet = Timer.getFPGATimestamp();
    }

    //sets motors via speeds in RPM
    public double speedSetpoint;
    public void setSpeed(double ccwSpeed, double cwSpeed){
        if (cals.DISABLED) return;

        if(ccwSpeed == 0 && cwSpeed == 0){
            setPower(0, 0);
        } else {
            ccwMotor.setSpeed(ccwSpeed);
            cwMotor.setSpeed(cwSpeed);
        }

        speedSetpoint = Math.max(ccwSpeed, cwSpeed);
    }

    public double getPrimeTime(){
        if(speedSetpoint < cals.minPrimeTimeSpd) return cals.minPrimeTime15h;
        if(speedSetpoint > cals.maxPrimeTimeSpd) return cals.minPrimeTime21h;
        //interp between the two wait times
        return (speedSetpoint - cals.minPrimeTimeSpd) / (cals.maxPrimeTimeSpd - cals.minPrimeTimeSpd) 
                * (cals.minPrimeTime21h - cals.minPrimeTime15h) + cals.minPrimeTime15h;
    }

    public void setPower(double ccwPower, double cwPower){
        if(cals.DISABLED) return;

        ccwMotor.setPower(ccwPower);
         cwMotor.setPower(cwPower);
    }

    public boolean upToSpeed(){
        if(cals.DISABLED) return false;

        double error = ccwMotor.getClosedLoopError();
        error += cwMotor.getClosedLoopError();

        return error > cals.minShootSpeedError && error < cals.maxShootSpeedError;
    }

    public boolean angleAligned(){
        if(cals.DISABLED) return false;

        double error = angleMotor.getClosedLoopError() * 360;
        return error > cals.minShootAngleError && error < cals.maxShootAngleError;
    }

    public void jogSpeed(boolean up){
        if (cals.DISABLED) return;
        if(up){
            jogSpeed += cals.jogSpeedInterval;
        } else {
            jogSpeed -= cals.jogSpeedInterval;
        }
    }

    public void jogSpeedUp(){
        jogSpeed(true);
    }

    public void jogSpeedDn(){
        jogSpeed(false);
    }

    public void jogAng(boolean forward){
        if (cals.DISABLED) return;
        if(forward){
            jogAng += cals.jogAngInterval;
        } else {
            jogAng -= cals.jogAngInterval;
        }
    }

    public void jogAngFwd(){
        jogAng(true);
    }

    public void jogAngBack(){
        jogAng(false);
    }

    public double getShooterAngle(){
        return angleMotor.getPosition()*360 - cals.angOffset;
    }

    public void transport(){
        if (cals.DISABLED) return;
        transpMotor.setPower(cals.tranPwr);
    }

    public void transport(double pwr){
        if (cals.DISABLED) return;
        transpMotor.setPower(pwr);
    }

    public void stopTransport(){
        if(cals.DISABLED) return;
        transpMotor.setPower(0);
    }

    private boolean cargoReadyToTP;
    public void preLoadCargo(){
        cargoReadyToTP = true;
    }

    double prevAnalogAngle;
    public void resetShooterAngle(){
        if(!potDisabled){
            double analogAngle = (anglePot.getVoltage() - cals.potVoltOffset)  / cals.voltsPerDegree + cals.sensorResetAngle;
            SmartDashboard.putNumber("AnalogShootAngle", analogAngle);
            if(first) prevAnalogAngle = analogAngle;

            //check if pot value is reasonable
            if(Math.abs(analogAngle - prevAnalogAngle) > cals.potMaxMovement){
                //moved too fast
                potDisabled = true;
            }
            if(analogAngle > cals.potMaxAngle || analogAngle < cals.potMinAngle){
                //out of bounds
                potDisabled = true;
            }
            Log.addValue(potDisabled, "Pot Broke", Log.compTab);

            //determine if we should rezero neo encoder
            double deltaAngle = Math.abs(angleMotor.getPosition() * 360 - analogAngle);
            boolean rezeroNeeded = deltaAngle > cals.potResetDeltaAngle;

            SmartDashboard.putNumber("analogShootErr", deltaAngle);
            
            //rezero
            if(rezeroNeeded){
                angleMotor.setEncoderPosition(analogAngle / 360);
            }
        }
    }

    private double preLoadTimer;
    private int preLoadRan;
    private double climbStartTime = 0;
    private boolean climbStartTimeRan;
    public void periodic(){
        if (cals.DISABLED) return;

        resetShooterAngle();

        SmartDashboard.putNumber("ShootSpeed", cwMotor.getSpeed());

        SmartDashboard.putNumber("Shooter Angle", getShooterAngle());
        Log.addValue(jogSpeed, "Shoot jogSpeed", Log.compTab);
        Log.addValue(jogAng, "Shoot jogAngle", Log.compTab);

        double speed = r.inputs.driverJoy.getDial1() * 
                    (cals.maxVariableShootSpeed - cals.minVariableShootSpeed)
                     + cals.minVariableShootSpeed;
        double angle = r.inputs.driverJoy.getDial2() *
                    (cals.shootMaxAngle - cals.shootMinAngle)
                    + cals.shootMinAngle;
        SmartDashboard.putNumber("Speed Dial Shoot", speed);
        SmartDashboard.putNumber("Angle Dial Shoot", angle);
        SmartDashboard.putNumber("ShooterSpeedError", cwMotor.getClosedLoopError() + ccwMotor.getClosedLoopError());

        if(cargoReadyToTP){
            cargoReadyToTP = false;
            preLoadTimer = Timer.getFPGATimestamp() + cals.preLoadTime;
            preLoadRan = 0;
        }

        if(Timer.getFPGATimestamp() < preLoadTimer && !r.sensors.ballSensorUpper.get()){
            //transport();
            //System.out.println("Ran preload");
            fire(cals.preLoadPower);
            //preLoadRan = 0;
        } else if(preLoadRan < 5) {
            //System.out.println("Stopped preload");
            fire(0);
            preLoadRan++;
        }

        if(Timer.getFPGATimestamp() > timeAngleWasSet + cals.maxAngleSetTime){
            angleMotor.setPower(0);
        }

        if(r.inputs.operatorJoy.climbSwitch() && climbStartTime + r.climb.cals.cannonAngTime > Timer.getFPGATimestamp()){
            if(!climbStartTimeRan){
                climbStartTime = Timer.getFPGATimestamp();
            }
            climbStartTimeRan = true;
            prime(0, cals.climbCannonAng);
        } else if(!r.inputs.operatorJoy.climbSwitch()){
            climbStartTimeRan = false;
        }

        first = false;
    }

    @Override
    public void close() throws Exception {
        cwMotor.close();
        ccwMotor.close();
        angleMotor.close();
        leftFireMotor.close();
        rightFireMotor.close();
        transpMotor.close();
    }
}
