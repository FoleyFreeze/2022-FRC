package frc.robot.Util;

public class Vector {
    
    public double r;
    public double theta;

    Vector vector;

    public Vector(double r, double theta){
        this.r = r;
        this.theta = theta; //note this is radians
    }

    //returns a vector from x and y polar coordinates
    public static Vector fromXY(double x, double y){

        double r = Math.sqrt((x*x) + (y*y));
        double theta = Math.atan2(y, x);

        return new Vector(r, theta);
    }

    //adds two given vectors using vector addition by breaking into x and y components
    public static Vector addVectors(Vector v1, Vector v2){

        double x = (Math.cos(v1.theta) * v1.r) + (Math.cos(v2.theta) * v2.r);
        double y = (Math.sin(v1.theta) * v1.r) + (Math.sin(v2.theta) * v2.r);

        double r = Math.sqrt((x * x) + (y * y));
        double theta = Math.atan2(y, x);

        return new Vector(r, theta);
    }

    //subtracts two given vectors using vector subtraction by breaking into x and y components
    public static Vector subVectors(Vector v1, Vector v2){

        double x = (Math.cos(v1.theta) * v1.r) - (Math.cos(v2.theta) * v2.r);
        double y = (Math.sin(v1.theta) * v1.r) - (Math.sin(v2.theta) * v2.r);

        double r = Math.sqrt((x * x) + (y * y));
        double theta = Math.atan2(y, x);

        return new Vector(r, theta);
    }

    public double getX(){
        return r * Math.cos(theta);
    }

    public double getY(){
        return r * Math.sin(theta);
    }
}
