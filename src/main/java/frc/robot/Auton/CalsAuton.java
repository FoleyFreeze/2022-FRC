package frc.robot.Auton;

import frc.robot.Util.Vector;

public class CalsAuton {

    public static double autoShootAngleKP = 0.01;
    public static double autoShootAngleMaxPwr = 0.3;
    public static double minShootDist = 15;

    public static double primeTime;
    public static double shootTime;
    public static double simpleShootFirePwr;
    public static double simpleShootPrimePwr;
    public static double simpleShootPrimeAng;

    public class Position{
        public Vector v;
        public double a;        
        
        public Position(Vector v, double a){
            this.v = v;
            this.a = a;
        }
    }

    int arrayLen = 10; //ensure that the position and skip lists are the same length
    Position[][] positionList = {
        { //left start position
            new Position(Vector.fromXY(0,0), -133.5), //don't move, just shoot (ABS)
            new Position(Vector.fromXY(-14.5, -19), -93.6), //grab close ball (REL)
            new Position(Vector.fromXY(76.2, -239.4), -45), //move to loading station (ABS)
            new Position(Vector.fromXY(40.0, -110.0), 112), //go back to shoot again (ABS)
        },

        { //mid start position
            new Position(Vector.fromXY(0,0), -46.5), //don't move, just shoot (REL)
            new Position(Vector.fromXY(16.3, -13.5), -85.0), //grab close ball (REL)
            new Position(Vector.fromXY(76.2, -239.4), -45), //move to loading station (ABS)
            new Position(Vector.fromXY(40.0, -110.0), 112), //go back to shoot again (ABS)
        },

        { //right start position
            new Position(Vector.fromXY(0,0), 1.5), //don't move, just shoot (REL)
            new Position(Vector.fromXY(-14.5, -19), -52.0), //grab close ball (REL)
            new Position(Vector.fromXY(118.3, -77.3), -123),//grab second ball (ABS)
            new Position(Vector.fromXY(76.2, -239.4), -45), //move to loading station (ABS)
            new Position(Vector.fromXY(40.0, -110.0), 112), //go back to shoot again (ABS)
        }
    };
    
    boolean[][] skipLists = {
        extendFalse(true),
        extendFalse(),
        extendFalse(),
        extendFalse(),
        extendFalse(),
        extendFalse(),
        extendFalse(),
    };

    private boolean[] extendFalse(boolean... val){
        boolean[] array = new boolean[arrayLen];
        int i=0;
        for(;i<val.length;i++){
            array[i] = val[i];
        }
        for(;i<arrayLen;i++){
            array[i] = false;
        }

        return array;
    }
}
