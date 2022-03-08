package frc.robot.Sensors;

import frc.robot.Robot;
import frc.robot.Util.Vector;

public class CalsSensors {

    public final boolean DISABLED = false && Robot.isReal();

    public final double VISION_DATA_TIMEOUT = 0.5;

    public final double MOVING_TIMEOUT = 0.5;

    public final int HISTORY_SIZE = 25;

    //distance from center of robot
    public Vector ballCamLocationL = Vector.fromXY(-6, 12.5);
    public Vector ballCamLocationR = Vector.fromXY(11, 12.5);
    public Vector tgtCamLocation = Vector.fromXY(0, 0);

    public CalsSensors(){

    }
}
