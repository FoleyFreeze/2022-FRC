package frc.robot.Sensors;

import frc.robot.Robot;

public class CalsSensors {

    public final boolean DISABLED = true && Robot.isReal();

    public final double VISION_DATA_TIMEOUT = 0.5;

    public final double MOVING_TIMEOUT = 0.5;

    public final int HISTORY_SIZE = 10;

    public CalsSensors(){

    }
}
