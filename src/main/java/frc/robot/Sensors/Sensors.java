package frc.robot.Sensors;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Sensors.Vision.VisionData;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;

public class Sensors extends SubsystemBase {

    public CalsSensors cals;

    public Vision vision = new Vision();
    public VisionData alliedCargo;
    public VisionData opponentCargo;

    public Vector botLoc;
    public double botAng;

    public Sensors(CalsSensors cals){
        this.cals = cals;

        alliedCargo = new VisionData();
        alliedCargo.timestamp = -10;
        opponentCargo = new VisionData();
        opponentCargo.timestamp = -10;
    }

    @Override
    public void periodic(){
        //update robot orientation and location
        //update history array of robot positions and orientations
        
        

        //process all camera data (and update robot location again?)
        //update the history with any modifications to the robot location?
        
        

        //process other sensors if any

    }

    public Vector toFieldCoord(Vector v){ 
        v.theta = Angle.normRad(v.theta + Math.toRadians(-1));
        v.add(botLoc);
        return v;
    }

    public boolean hasAlliedCargo(){
        return Timer.getFPGATimestamp() - alliedCargo.timestamp < cals.DATA_TIMEOUT;
    }

    public Vector getAlliedCargoFieldPos(){
        Vector v = alliedCargo.botRelativeLoc;
        //v.r -= getFieldOrientAngle();

        Vector roboVec = new Vector(0, 0);

        return Vector.subVectors(v, roboVec);
    }
}
