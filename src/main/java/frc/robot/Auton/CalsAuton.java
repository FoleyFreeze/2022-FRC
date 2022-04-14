package frc.robot.Auton;

import frc.robot.Util.EditableCal;
import frc.robot.Util.Vector;

public class CalsAuton {

    public static boolean DEBUG = false;
    public static boolean useCamera = false;
    public static double autonDist;

    public static double autoShootAngleKP = 0.01;
    public static double autoShootAngleKD = 0.0;
    public static double autoShootTurnMaxPwr = 0.3;
    public static double minShootDist = 15;

    public static double primeTime = 0.5;
    public static double shootTime = 0.75;
    public static double joeShotFirePwr = 1; //simple shoot
    public static double joeShotPrimeSpeed = 1375; //simple shoot
    public static double joeShotPrimeAng = 70+2; //simple shoot

    public static double maxDrivePower = 0.27;
    public static double maxSwervePower = 0.25;
    public static double minAutoPosError = 6;
    public static double minAutoAngError = 10;
    public static double autoSwerveKP = 0.010;
    public static double autonDriveTime = 5;

    //motion profiling
    static boolean motionProfileEditable = DEBUG;

    public static EditableCal mp_kA = new EditableCal("MP_kA", 0.0009, motionProfileEditable); //power per in/sec^2
    public static EditableCal mp_kV = new EditableCal("MP_kV", 0.006, motionProfileEditable); //power per in/sec
    public static EditableCal mp_kS = new EditableCal("MP_kS", 0.0247, motionProfileEditable);  //power
    public static EditableCal mp_MaxVel = new EditableCal("MP_MaxVel", 140, motionProfileEditable);
    public static EditableCal mp_MaxAccel = new EditableCal("MP_MaxAccel", 115, motionProfileEditable);
    public static EditableCal mp_kP = new EditableCal("MP_kP", 0.025, motionProfileEditable);
    public static EditableCal mp_MaxPwr = new EditableCal("MP_MaxPwr", 1, motionProfileEditable);

    public class Position{
        public Vector v;
        public double a;        
        
        public Position(Vector v, double a){
            this.v = v;
            this.a = a;
        }
    }

    //zero degrees is forward

    static final int arrayLen = 8; //ensure that the position and todo lists are the same length
    Position[][] positionList = {
        { //left start position
            new Position(Vector.fromXY(-64.5, -64.5), -133.5 -90), //init position
            null, //simple shoot
            null, //wait time
            new Position(Vector.fromXY(-48, -48), 160), //move after 1-ball auton (REL)
            new Position(Vector.fromXY(-14.5, -19), -93.6 - 90), //grab close ball (REL)
            //NOTE THE BELOW POSITION MIGHT BE WRONG
            new Position(Vector.fromXY(118.3, -77.3), -123 - 90), //grab second ball (ABS)
            new Position(Vector.fromXY(76.2, -239.4), -45 - 90), //move to loading station (ABS)
            new Position(Vector.fromXY(40.0, -110.0), 112 - 90), //go back to shoot again (ABS)
        },

        { //mid start position
            new Position(Vector.fromXY(64.5, -64.5), -46.5 - 90), //init position
            null, //simple shoot
            null, //wait time
            new Position(Vector.fromXY(0, -48), -90 -90), //move after 1-ball auton (REL)
            new Position(Vector.fromXY(16.3, -13.5), -85.0 - 90), //grab close ball (REL)
            //NOTE THE BELOW POSITION MIGHT BE WRONG
            new Position(Vector.fromXY(118.3, -77.3), -123 - 90), //grab second ball (ABS)
            new Position(Vector.fromXY(76.2, -239.4), -45 - 90), //move to loading station (ABS)
            new Position(Vector.fromXY(40.0, -110.0), 112 - 90), //go back to shoot again (ABS)
        },

        { //right start position
            new Position(Vector.fromXY(91.2, 4.9), 1.5 - 90), //init position
            null, //simple shoot
            null, //wait time
            new Position(Vector.fromXY(48, 0), -90), //move after 1-ball auton (REL)
            //new Position(Vector.fromXY(27, -4), -52.0 - 90), //grab close ball (REL)
            new Position(Vector.fromXY(50, -32), -118), //grab close ball (REL)
            new Position(Vector.fromXY(118.3+8.0, -77.3), -123 - 90), //grab second ball (ABS)
            new Position(Vector.fromXY(76.2, -239.4), -45 - 90), //move to loading station (ABS)
            new Position(Vector.fromXY(40.0, -110.0), 112 - 90), //go back to shoot again (ABS)
        },

        { //test start positions
            new Position(Vector.fromXY(0,120), 0),
            null,
            null,
            null,
            null,
            null,
            null,
            null
        }
    };
    
    boolean[][] todoLists = {
        extendFalse(true), //do nothing, reset position
        extendFalse(true, true), //1-ball
        extendFalse(true, true, true, true), //1-ball, drive
        extendFalse(true, true, false, false, true), //2-ball
        extendFalse(true, true, false, false, true, true), //3-ball close
        extendFalse(true, true, false, false, true, false, true), //3-ball far
        extendFalse(true, true, false, false, true, true, true, true), //4-ball close
        extendFalse(true, true, false, false, true, false, true, true), //4-ball far
        extendFalse(true, true, false, false, true, false, true, true), //5-ball
    };

    static final int manArrayLen = 11; //ensure that the position and skip lists are the same length
    Position[][] manPositionList = {
        { //left start position MANUAL, NO CAMERA
            new Position(Vector.fromXY(-64.5, -64.5), 136.5), //init position
            null, //simple shoot
            null, //wait time
            new Position(Vector.fromXY(-48, -48), 136.5), //move after 1-ball auton (REL)
            new Position(Vector.fromXY(162-244+5, -129), 136.5), //grab close ball (ABS)
            new Position(Vector.fromXY(-64.5-5,-64.5-18), 136.5),//shoot for 2 ball
            new Position(Vector.fromXY(-152.5, -34), 70), //grab ball 3
            new Position(Vector.fromXY(-117, -100), 130), //shoot ball 3 (but not if 4 ball)
            new Position(Vector.fromXY(-117, -100), 90), //drive to hit opponent ball
            null,
            new Position(Vector.fromXY(-64.5-5,-64.5-18), 136.5), //drive and shoot ball 3 (if 4 ball)
        },

        { //mid start position MANUAL, NO CAMERA
            new Position(Vector.fromXY(64.5, -64.5), -46.5 - 90), //init position
            null, //simple shoot
            null, //wait time
            new Position(Vector.fromXY(0, -48), -46.5 - 90), //move after 1-ball auton (REL)
            null,
            null,
            null,
            null,
            null,
            null,
            null
        },

        { //right start position MANUAL, NO CAMERA
            new Position(Vector.fromXY(91.2, 4.9), 1.5 - 90), //init position
            null, //simple shoot
            null, //wait time
            new Position(Vector.fromXY(40, 0), -90), //move after 1-ball auton (REL)
            new Position(Vector.fromXY(91.2+50-1, 4.9-32-3), -90), //grab close ball (ABS)
            new Position(Vector.fromXY(90+15,5-7), -90),//shoot for 2 ball (ABS)
            new Position(Vector.fromXY(162-74-5, -125), 155+8+8), //grab second ball (ABS)
            new Position(Vector.fromXY(77.5, -140), -151), //better shooting position
            //new Position(Vector.fromXY(75,-75), -132),//move then shoot balls 2-3 (ABS)
            new Position(Vector.fromXY(119-3, -283), -165), //move to loading station (ABS)
            null,
            //new Position(Vector.fromXY(75.0, -75), -132), //go back to shoot again (ABS)
            new Position(Vector.fromXY(77.5, -140), -151)
        },

        { //test start positions
            new Position(Vector.fromXY(0, 0), 180), //init position
            null,
            null,
            new Position(Vector.fromXY(0, -48), 180), //move after 1-ball auton (REL)
            null,
            null,
            null,
            null,
            null,
            null,
            null
        }
    };
    
    boolean[][] manTodoLists = {
        extendFalse(true), //do nothing, reset position
        //extendFalse(false, false, false, true), //temp for testing only
        extendFalse(true, true), //1-ball
        extendFalse(true, true, true, true), //1-ball, drive
        extendFalse(true, true, false, false, true, true), //2-ball
        extendFalse(true, true, false, false, true, false, true, true), //3-ball close (right only)
        extendFalse(true, true, false, false, true, true, true, true), //3-ball far (left only)
        extendFalse(true, true, false, false, true, true, true, false, true, false, true), //4-ball close (left only)
        extendFalse(true, true, false, false, true, false, true), //4-ball far
        extendFalse(true, true, false, false, true, false, true, true, true, true, true)// 5-ball
    };

    private boolean[] extendFalse(boolean... val){
        int arrayLength = Math.max(arrayLen, manArrayLen);

        boolean[] array = new boolean[arrayLength];
        //System.out.println(val.length + " " + arrayLength + " " + manArrayLen);
        int i=0;
        for(;i<val.length;i++){
            array[i] = val[i];
        }
        for(;i<arrayLength;i++){
            array[i] = false;
        }

        return array;
    }
}
