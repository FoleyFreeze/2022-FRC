package frc.robot.Util;

public class Angle {
    
    //an angle will always be between -180 and +180
    public static double normDeg(double angle){
        angle %= 360;
        
        if(angle > 180){
            angle -= 360;
        }
        return angle;
    }

    public static double normRad(double angle){
        angle %= Math.PI * 2;
        
        if(angle > Math.PI){
            angle -= Math.PI * 2;
        }
        return angle;
    }

}
