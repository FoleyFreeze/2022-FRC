package frc.robot.Inputs;

public class CalsInputs {

    public final boolean DEBUG = false;
    public final double CHECK_INTERVAL = 2;

    //flysky cals
    public final double FS_DEADBAND_UPPER = 0.9;
    public final double FS_DEADBAND_LOWER = 0.075;
    public final double FS_EXPO = 1.8;
    public final double FS_INIT_VALUE = 0.03;

    public final int FS_FIELD_ORIENT = 5;
    public final int FS_RESET_SWERVE_LEFT = 11;
    public final int FS_RESET_SWERVE_RIGHT = 15;
    public final double RESET_ANGLE_DELAY = 5;

    public final int FS_RESET_NAVX_ANG = 10;
    public final int FS_RESET_NAVX_POS = 14;
    public final double RESET_NAVX_DELAY = 3;

    //driverstation cals
    public final int CB_PRIME = 0;
    public final int CB_FIRE = 0;

    public CalsInputs(){

    }
}
