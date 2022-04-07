package frc.robot.Sensors;

import frc.robot.Robot;
import frc.robot.Util.EditableCal;
import frc.robot.Util.Vector;

public class CalsSensors {

    public final boolean DISABLED = false && Robot.isReal();

    public final EditableCal VISION_DATA_TIMEOUT = new EditableCal("VisionTimeout", 0.2);

    public final double MOVING_TIMEOUT = 0.5;

    public final int HISTORY_SIZE = 25;

    //distance from center of robot
    public Vector ballCamLocationL = Vector.fromXY(-11, 12.5);
    public Vector ballCamLocationR = Vector.fromXY(11, 12.5);
    public Vector tgtCamLocation = Vector.fromXY(0, -11.5);
    public double ballCamAngleL = 90;
    public double ballCamAngleR = 85;
    public double tgtCamAngle = -90;

    public EditableCal switchablePower = new EditableCal("SwitchedPwr", 1, false);

    public EditableCal forceTgtLights = new EditableCal("ForceTgtLghts", 0);
    public EditableCal forceBallLights = new EditableCal("ForceBallLghts", 0);

    public CalsSensors(){

    }
}
