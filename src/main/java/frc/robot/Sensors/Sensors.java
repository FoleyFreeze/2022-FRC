package frc.robot.Sensors;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Sensors.GPS.CameraGPS;
import frc.robot.Sensors.GPS.NavX;
import frc.robot.Sensors.GPS.SwerveEncoder;
import frc.robot.Sensors.Vision.VisionData;
import frc.robot.Util.Angle;
import frc.robot.Util.Log;
import frc.robot.Util.Vector;

public class Sensors extends SubsystemBase implements AutoCloseable{

    public CalsSensors cals;

    public Vision vision = new Vision();
    public VisionData alliedCargo;
    public VisionData opponentCargo;
    public VisionData target;

    public CameraGPS camera;
    public NavX navX;
    public SwerveEncoder encoders;

    public Vector botLoc;
    public Vector prevBotLoc;
    public double botAng;
    public double prevBotAng;

    public boolean isOnRedTeam;
    private boolean checkedAlliance;

    public Sensors(CalsSensors cals, RobotContainer c){
        this.cals = cals;
        checkedAlliance = false;

        alliedCargo = new VisionData();
        alliedCargo.timestamp = -10;
        opponentCargo = new VisionData();
        opponentCargo.timestamp = -10;

        camera = new CameraGPS(cals.HISTORY_SIZE);
        navX = new NavX();
        encoders = new SwerveEncoder(c.drive.wheels);
    }

    @Override
    public void periodic(){
        if(!checkedAlliance){
            switch(DriverStation.getAlliance()){
                case Blue:
                    isOnRedTeam = false;
                    checkedAlliance = true;
                    break;
                case Invalid: 
                    break;
                case Red:
                    isOnRedTeam = true;
                    checkedAlliance = true;
                    break;
            }
        }

        Log.logBool(navX.navX.isConnected(), Log.LOG_GROUPS.SENSORS, 1, true, "navX Connected");

        //update robot orientation and location
        encoders.updateRobotLocation();

        Log.logString(encoders.botPos.toStringXY(), Log.LOG_GROUPS.SENSORS, 1, true, "encoder X, Y");
        Log.logDouble(encoders.botAng, Log.LOG_GROUPS.SENSORS, 1, true, "encoder angle");

        boolean isMoving = botIsMoving();

        Log.logDouble(navX.getFieldOrientAngle(), Log.LOG_GROUPS.SENSORS, 1, true, "navx angle");
        Log.logString(navX.getFieldOrientDisplacement(isMoving).toStringXY(), Log.LOG_GROUPS.SENSORS, 1, true, "navx X, Y");

        //TODO: figure out a better filtering strategy?
        botLoc = new Vector(encoders.botPos);
        botAng = navX.getFieldOrientAngle();

        //update history array of robot positions and orientations
        camera.addLocation(botLoc, botAng, Timer.getFPGATimestamp());

        //process all camera data (and update robot location again?)

        while(!vision.visionQueue.isEmpty()){
            VisionData vd = vision.visionQueue.poll();
            camera.imgToLocation(vd);
            switch(vd.type){
                case BLUE_CARGO:
                    if(isOnRedTeam) opponentCargo = vd;
                    else alliedCargo = vd;
                    break;
                case RED_CARGO:
                    if(!isOnRedTeam) opponentCargo = vd;
                    else alliedCargo = vd;
                    break;
                case VISION_TARGET:
                    //maybe do a blend or something based on percieved accuracy of the image
                    camera.updateArray(target.location);

                    //also remove any error that was present in the calculated cargo locations
                    if(alliedCargo != null) alliedCargo.location.add(target.location);
                    if(opponentCargo != null) opponentCargo.location.add(target.location);
                    break;
            }
        }

        //process other sensors if any

    }

    public void resetAng(){
        navX.resetAng();
        encoders.resetAng();
    }

    public void resetPos(){
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
        Vector v = Vector.subVectors(encoders.botPos, prevEncBotPos);
        if(v.r != 0){
            //we have moved
            timeOfMovement = Timer.getFPGATimestamp();
        } 

        return Timer.getFPGATimestamp() - timeOfMovement < cals.MOVING_TIMEOUT;
    }

    public boolean hasAlliedCargo(){
        return Timer.getFPGATimestamp() - alliedCargo.timestamp < cals.VISION_DATA_TIMEOUT;
    }

    public boolean hasGatheredCargo(){
        return false;
    }

    @Override
    public void close() throws Exception {
        encoders.close();
        navX.close();
    }
}
