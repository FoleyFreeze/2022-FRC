package frc.robot.Inputs;

public class CalsFlysky {

    public final int TOP_LEFT_SWITCH = 1 << 0;
    public final int TOP_LEFT_3POS_UP = 1 << 1;
    public final int TOP_LEFT_3POS_DOWN = 1 << 2;
    public final int TOP_LEFT_HAT_UP = 1 << 9;
    public final int TOP_LEFT_HAT_LEFT = 1 << 12;
    public final int TOP_LEFT_HAT_DOWN = 1 << 10;
    public final int TOP_LEFT_HAT_RIGHT = 1 << 11;
    public final int BOT_LEFT_3POS_UP = 1 << 5;
    public final int BOT_LEFT_3POS_DOWN = 1 << 6;

    public final int TOP_RIGHT_SWITCH = 1 << 4; 
    public final int TOP_RIGHT_LEVER = 1 << 3;
    public final int TOP_RIGHT_HAT_UP = 1 << 13;
    public final int TOP_RIGHT_HAT_LEFT = 1 << 16;
    public final int TOP_RIGHT_HAT_DOWN = 1 << 14;
    public final int TOP_RIGHT_HAT_RIGHT = 1 << 15;
    public final int BOT_RIGHT_3POS_UP = 1 << 7;
    public final int BOT_RIGHT_3POS_DOWN = 1 << 8;

    public final int LEFT_DIAL = 7;
    public final int RIGHT_DIAL = 6;
    public final int LEFT_TRIGGER = 2;
    public final int RIGHT_TRIGGER = 3;

    public final int X_AXIS = 0;
    public final int Y_AXIS = 1;
    public final int Z_AXIS = 4;



    public final int resetAngle = TOP_LEFT_HAT_UP;
    public final int resetPosition = TOP_RIGHT_HAT_UP;
    public final int learnWheelPositions = TOP_LEFT_HAT_DOWN | TOP_RIGHT_HAT_DOWN;
    public final double wheelLearnWaitTime = 3;
    public final int fieldOrient = TOP_RIGHT_SWITCH;

    public final int fireCannon = RIGHT_TRIGGER;
    public final int cameraShoot = TOP_RIGHT_SWITCH;
    public final int loadCargo = TOP_RIGHT_LEVER;
    public final int resetCannon = TOP_RIGHT_HAT_RIGHT | TOP_LEFT_HAT_RIGHT;
    public final int sensorResetCannon = TOP_RIGHT_HAT_LEFT | TOP_LEFT_HAT_LEFT;

    public final int intake = LEFT_TRIGGER;
    public final int manualIntake = TOP_LEFT_3POS_DOWN;
    public final int intakeSpin = BOT_LEFT_3POS_DOWN;
    public final int fireSpin = BOT_LEFT_3POS_UP;
        

    public CalsFlysky(){

    }
}
