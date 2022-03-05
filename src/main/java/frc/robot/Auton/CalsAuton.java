package frc.robot.Auton;

import frc.robot.Util.Vector;

public class CalsAuton {

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
            new Position(Vector.fromXY(0,0), 0), //drive to the ball right behind us
            new Position(Vector.fromXY(0,0), 0), //drive to the loading station
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0)
        },

        { //mid start position
            new Position(Vector.fromXY(0,0), 0), //drive to the ball right behind us
            new Position(Vector.fromXY(0,0), 0), //drive to the loading station
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0)
        },

        { //right start position
            new Position(Vector.fromXY(0,0), 0), //drive to the ball right behind us
            new Position(Vector.fromXY(0,0), 0), //drive to the loading station
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0),
            new Position(Vector.fromXY(0,0), 0)
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
