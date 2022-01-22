package frc.robot.Util;

public class Vector {
    
    public double r;
    public double theta;

    Vector vector;

    public Vector(double r, double theta){
        this.r = r;
        this.theta = Angle.normRad(theta); //note this is radians
    }
   
    public Vector inverse(){
        r = -r;
        theta += Math.PI;

        return this;
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

    public Vector add(Vector v){
        double x = (Math.cos(theta) * r) + (Math.cos(v.theta) * v.r);
        double y = (Math.sin(theta) * r) + (Math.sin(v.theta) * v.r);

        r = Math.sqrt((x * x) + (y * y));
        theta = Angle.normRad(Math.atan2(y, x));

        return this;
    }

    public Vector subtract(Vector v){
        double x = (Math.cos(theta) * r) - (Math.cos(v.theta) * v.r);
        double y = (Math.sin(theta) * r) - (Math.sin(v.theta) * v.r);

        r = Math.sqrt((x * x) + (y * y));
        theta = Angle.normRad(Math.atan2(y, x));

        return this;
    }

    //returns a string of r, theta
    public String toStringPolar(){
        return String.format("%.2f,%.0f\u00b0", r, Math.toDegrees(theta));
    }

    //returns a string of x, y
    public String toStringXY(){
        return String.format("%.2f,%.2f", getX(), getY());
    }
}
