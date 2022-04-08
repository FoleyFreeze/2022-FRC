package frc.robot.Sensors;

import java.util.function.DoubleConsumer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Sensors.GPS.CameraGPS;
import frc.robot.Sensors.GPS.NavX;
import frc.robot.Sensors.GPS.SwerveEncoder;
import frc.robot.Sensors.Vision.VisionData;
import frc.robot.Util.Angle;
import frc.robot.Util.Log;
import frc.robot.Util.SmartDigitalInput;
import frc.robot.Util.Vector;

public class Sensors extends SubsystemBase implements AutoCloseable{

    public CalsSensors cals;
    RobotContainer r;

    public Vision vision;
    public VisionData alliedCargo;
    public VisionData opponentCargo;
    public VisionData target;

    public CameraGPS camera;
    public NavX navX;
    public SwerveEncoder encoders;

    public Vector botLoc;
    public Vector prevBotLoc;
    public double botAng;
    public double dBotAng;
    public double prevBotAng;

    public boolean isOnRedTeam;

    public SmartDigitalInput ballSensorUpper;
    public SmartDigitalInput ballSensorLower;
    public SmartDigitalInput cannonAngleSensor;

    public PowerDistribution pdh;
    public Spark blinken;

    public PneumaticHub pcm;
    Solenoid tgtLights;
    Solenoid ballLights;

    public Sensors(CalsSensors cals, RobotContainer r){
        this.cals = cals;
        this.r = r;
        vision = new Vision(cals);
        if(cals.DISABLED == true) return;

        alliedCargo = new VisionData(); 
        alliedCargo.timestamp = -10;
        opponentCargo = new VisionData();
        opponentCargo.timestamp = -10;
        target = new VisionData();
        target.timestamp = -10;

        camera = new CameraGPS(cals.HISTORY_SIZE);
        navX = new NavX();
        encoders = new SwerveEncoder(r.drive.wheels);

        ballSensorUpper = new SmartDigitalInput(8);
        ballSensorLower = new SmartDigitalInput(9);
        cannonAngleSensor = new SmartDigitalInput(20);
        cannonAngleSensor.invert();

        pdh = new PowerDistribution();

        blinken = new Spark(9);

        pcm = new PneumaticHub();
        tgtLights = pcm.makeSolenoid(1);
        ballLights = pcm.makeSolenoid(2);

        cals.forceTgtLights.addCallback(new DoubleConsumer() {
            public void accept(double d){
                //enableTgtLights(d > 0);
                tgtLights.set(d > 0);
            }
        });
        cals.forceBallLights.addCallback(new DoubleConsumer() {
            public void accept(double d){
                //enableCargoLights(d > 0);
                ballLights.set(d > 0);
            }
        });
    }

    public double dt;
    double prevTime;

    @Override
    public void periodic(){
        double time = Timer.getFPGATimestamp();
        dt = time - prevTime;
        prevTime = time;
        SmartDashboard.putNumber("dt", dt);

        if(cals.DISABLED) return;

        vision.periodic();
        navX.periodic();

        ballSensorLower.update();
        ballSensorUpper.update();
        cannonAngleSensor.update();

        switch(DriverStation.getAlliance()){
            case Blue:
                isOnRedTeam = false;
                break;
            case Invalid: 
                break;
            case Red:
                isOnRedTeam = true;
                break;
        }

        //Log.logBool(navX.navX.isConnected(), Log.LOG_GROUPS.SENSORS, 1, true, "navX Connected");

        //update robot orientation and location
        if(!r.drive.cals.DISABLED){
            encoders.updateRobotLocation(navX.getFieldOrientAngle());
        }

        Log.logString(encoders.botPos.toStringXY(), Log.LOG_GROUPS.SENSORS, 1, true, "encoder X, Y");
        Log.logDouble(encoders.botAng, Log.LOG_GROUPS.SENSORS, 1, true, "encoder angle");

        boolean isMoving = botIsMoving();

        Log.logDouble(navX.getFieldOrientAngle(), Log.LOG_GROUPS.SENSORS, 1, true, "navx angle");
        Log.logString(navX.getFieldOrientDisplacement(isMoving).toStringXY(), Log.LOG_GROUPS.SENSORS, 1, true, "navx X, Y");

        botLoc = new Vector(encoders.botPos);
        botAng = navX.getFieldOrientAngle();
        dBotAng = Angle.normDeg(botAng - prevBotAng) / dt;
        prevBotAng = botAng;
        SmartDashboard.putNumber("dAngle", dBotAng);

        //update history array of robot positions and orientations
        if(!r.cannon.cals.DISABLED){
            camera.addLocation(botLoc, botAng, Timer.getFPGATimestamp(), r.cannon.angleMotor.getPosition() * 360);
        }
        //process all camera data (and update robot location again?)

        int count = 0;
        while(!vision.visionQueue.isEmpty() && count++ < 1){
            VisionData vd = vision.visionQueue.poll();
            //camera.imgToLocation(vd);
            switch(vd.type){
                case BLUE_CARGO:
                    camera.imgToLocation(vd);
                    SmartDashboard.putString("LastBlueLoc", vd.location.toStringXY());
                    if(isOnRedTeam) {
                        if(!hasOpponentCargo()) opponentCargo = vd;
                        else if(opponentCargo.camLocation == vd.camLocation) opponentCargo = vd;
                        else {
                            Vector v1 = Vector.subVectors(opponentCargo.location, botLoc);
                            Vector v2 = Vector.subVectors(vd.location, botLoc);
                            if(v1.r > v2.r) opponentCargo = vd;
                        }
                    } else {
                        if(!hasAlliedCargo()) alliedCargo = vd;
                        else if(alliedCargo.camLocation == vd.camLocation) alliedCargo = vd;
                        else {
                            Vector v1 = Vector.subVectors(alliedCargo.location, botLoc);
                            Vector v2 = Vector.subVectors(vd.location, botLoc);
                            if(v1.r > v2.r) alliedCargo = vd;
                        }
                    }
                    break;
                case RED_CARGO:
                    camera.imgToLocation(vd);
                    SmartDashboard.putString("LastRedLoc", vd.location.toStringXY());
                    if(!isOnRedTeam) {
                        if(!hasOpponentCargo()) opponentCargo = vd;
                        else if(opponentCargo.camLocation == vd.camLocation) opponentCargo = vd;
                        else {
                            Vector v1 = Vector.subVectors(opponentCargo.location, botLoc);
                            Vector v2 = Vector.subVectors(vd.location, botLoc);
                            if(v1.r > v2.r) opponentCargo = vd;
                        }
                    } else {
                        if(!hasAlliedCargo()) alliedCargo = vd;
                        else if(alliedCargo.camLocation == vd.camLocation) alliedCargo = vd;
                        else {
                            Vector v1 = Vector.subVectors(alliedCargo.location, botLoc);
                            Vector v2 = Vector.subVectors(vd.location, botLoc);
                            if(v1.r > v2.r) alliedCargo = vd;
                        }
                    }
                    break;
                case VISION_TARGET:
                    camera.imgToLocation(vd);
                    SmartDashboard.putString("LastTargetLoc", vd.location.toStringXY());

                    target = vd;
                    //reset robot position based on goal
                    //encoders.resetPos(Vector.subVectors(new Vector(0,0), target.location));

                    //maybe do a blend or something based on percieved accuracy of the image
                    //camera.updateArray(target.location);

                    //also remove any error that was present in the calculated cargo locations
                    //if(alliedCargo.location != null) alliedCargo.location.add(target.location);
                    //if(opponentCargo.location != null) opponentCargo.location.add(target.location);
                    break;
            }
        }
        vision.visionQueue.clear();

        //process other sensors if any

        SmartDashboard.putBoolean("Ball Sensor High", ballSensorUpper.get());
        SmartDashboard.putBoolean("Ball Sensor Low", ballSensorLower.get());
        SmartDashboard.putBoolean("Cannon Sensor", cannonAngleSensor.get());

        SmartDashboard.putNumber("Pitch", navX.pitch);
        SmartDashboard.putNumber("Pitch Rate", navX.pitchRate);

        pdh.setSwitchableChannel(cals.switchablePower.get() > 0);
        //pdh.setSwitchableChannel(r.inputs.driverJoy.cameraShoot() && r.inputs.driverJoy.fireCannon());
        /*if(r.inputs.driverJoy.cameraShoot() && r.inputs.driverJoy.fireCannon()){
            tgtLights.set(true);
        } else {
            tgtLights.set(false);
        }*/

        //lights
        int ballCt = 0;
        if(ballSensorLower.get()) ballCt++;
        if(ballSensorUpper.get()) ballCt++;

        if(DriverStation.isAutonomousEnabled()){
            blinken.set(LEDs.Fixed_Palette_Pattern_Rainbow_with_Glitter_Pattern_Density_Speed_Brightness);
        } else if(r.inputs.operatorJoy.climbSwitch()){
            blinken.set(LEDs.Fixed_Palette_Pattern_Rainbow_Party);
        } else if(ballCt == 0){
            blinken.set(LEDs.Solid_Colors_Red);
        } else if(ballCt == 1){
            blinken.set(LEDs.Solid_Colors_Yellow);
        } else if(ballCt == 2){
            blinken.set(LEDs.Solid_Colors_White);
        }
    }

    public void resetAng(){
        if(cals.DISABLED) return;
        navX.resetAng();
        encoders.resetAng();
    }

    public void resetPos(){
        if(cals.DISABLED) return;
        navX.resetPos();
        encoders.resetPos();
    }

    public Vector toFieldCoord(Vector v){ 
        v.theta = Angle.normRad(v.theta + Math.toRadians(-1));
        v.add(botLoc);
        return v;
    }

    //checks difference in positions and angles of the robot from this class to check if it is moving
    private Vector prevEncBotPos = new Vector(0,0);
    private double timeOfMovement = 0;
    public boolean botIsMoving(){
        if(cals.DISABLED) return false;
        Vector v = Vector.subVectors(encoders.botPos, prevEncBotPos);
        if(v.r != 0){
            //we have moved
            timeOfMovement = Timer.getFPGATimestamp();
        } 

        return Timer.getFPGATimestamp() - timeOfMovement < cals.MOVING_TIMEOUT;
    }

    public boolean hasAlliedCargo(){
        if(cals.DISABLED) return false;
        return Timer.getFPGATimestamp() - alliedCargo.timestamp < cals.VISION_DATA_TIMEOUT.get();
    }

    public boolean hasOpponentCargo(){
        if(cals.DISABLED) return false;
        return Timer.getFPGATimestamp() - opponentCargo.timestamp < cals.VISION_DATA_TIMEOUT.get();
    }

    public boolean hasTargetImage(){
        if(cals.DISABLED) return false;
        return Timer.getFPGATimestamp() - target.timestamp < cals.VISION_DATA_TIMEOUT.get();
    }

    public void enableCargoLights(boolean on){
        ballLights.set(on && r.inputs.cameraDrive());
    }

    public void enableTgtLights(boolean on){
        tgtLights.set(on && r.inputs.cameraDrive());
    }

    @Override
    public void close() throws Exception {
        encoders.close();
        navX.close();
        tgtLights.close();
        ballLights.close();
        blinken.close();
        ballSensorLower.close();
        ballSensorUpper.close();
        cannonAngleSensor.close();
        pdh.close();
        pcm.close();
    }
}
