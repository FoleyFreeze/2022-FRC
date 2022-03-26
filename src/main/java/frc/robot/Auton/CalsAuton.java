package frc.robot.Auton;

import frc.robot.Util.Vector;

public class CalsAuton {

    public static boolean useCamera = true;

    public static double autoShootAngleKP = 0.01;
    public static double autoShootAngleKD = 0.0;
    public static double autoShootTurnMaxPwr = 0.3;
    public static double minShootDist = 15;

    public static double primeTime = 0.75;
    public static double shootTime = 1.0;
    public static double joeShotFirePwr = 1; //simple shoot
    public static double joeShotPrimeSpeed = 1375; //simple shoot
    public static double joeShotPrimeAng = 70; //simple shoot

    public static double maxDrivePower = 0.20;
    public static double maxSwervePower = 0.20;
    public static double minAutoPosError = 6;
    public static double minAutoAngError = 10;
    public static double autoSwerveKP = 0.01;
    public static double autonDriveTime = 5;

    public class Position{
        public Vector v;
        public double a;        
        
        public Position(Vector v, double a){
            this.v = v;
            this.a = a;
        }
    }

    //zero degrees is forward

    int arrayLen = 12; //ensure that the position and todo lists are the same length
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
            new Position(Vector.fromXY(50, -4), 180), //grab close ball (REL)
            new Position(Vector.fromXY(118.3, -77.3), -123 - 90), //grab second ball (ABS)
            new Position(Vector.fromXY(76.2, -239.4), -45 - 90), //move to loading station (ABS)
            new Position(Vector.fromXY(40.0, -110.0), 112 - 90), //go back to shoot again (ABS)
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
    };

    //TODO: set manual positions & skip lists based on CAD models
    int manArrayLen = 12; //ensure that the position and skip lists are the same length
    Position[][] manPositionList = {
        { //left start position
            new Position(Vector.fromXY(-64.5, -64.5), -133.5 -90), //init position
            null, //simple shoot
            null, //wait time
            new Position(Vector.fromXY(0, 0), 0 - 90),
            new Position(Vector.fromXY(0, 0), 0 - 90),
            new Position(Vector.fromXY(0, 0), 0 - 90),
            new Position(Vector.fromXY(0, 0), 0 - 90),
        },

        { //mid start position
            new Position(Vector.fromXY(64.5, -64.5), -46.5 - 90), //init position
            null, //simple shoot
            null, //wait time
            new Position(Vector.fromXY(0, 0), 0 - 90),
            new Position(Vector.fromXY(0, 0), 0 - 90),
            new Position(Vector.fromXY(0, 0), 0 - 90),
            new Position(Vector.fromXY(0, 0), 0 - 90),
        },

        { //right start position
            new Position(Vector.fromXY(91.2, 4.9), 1.5 - 90), //init position
            null, //simple shoot
            null, //wait time
            new Position(Vector.fromXY(0, 0), 0 - 90),
            new Position(Vector.fromXY(0, 0), 0 - 90),
            new Position(Vector.fromXY(0, 0), 0 - 90),
            new Position(Vector.fromXY(0, 0), 0 - 90),
        }
    };
    
    boolean[][] manTodoLists = {
        extendFalse(true), //do nothing, reset position
        extendFalse(true, true), //1-ball
        extendFalse(true, true, true, true), //1-ball, drive
        extendFalse(true, true, false, false, true), //2-ball
        extendFalse(true, true, false, false, true, true), //3-ball close
        extendFalse(true, true, false, false, true, false, true), //3-ball far
        extendFalse(true, true, false, false, true, true, true, true), //4-ball close
        extendFalse(true, true, false, false, true, false, true, true), //4-ball far
    };

    private boolean[] extendFalse(boolean... val){
        int arrayLength = useCamera ? arrayLen : manArrayLen;

        boolean[] array = new boolean[arrayLength];
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
