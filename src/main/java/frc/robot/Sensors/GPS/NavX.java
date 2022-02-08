package frc.robot.Sensors.GPS;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.SerialPort.Port;
import frc.robot.Util.Vector;

public class NavX {

    public AHRS navX;
    public double prevDX;
    public double prevDY;
    public double prevAng;

    public NavX(){
        navX = new AHRS(Port.kUSB);
        navX.calibrate();
        navX.zeroYaw();
    }
    
    public double getFieldOrientAngle(){
        return -navX.getYaw() + prevAng;
    }

    public Vector getFieldOrientDisplacement(boolean isMoving){
        if(!isMoving){
            prevDX = Units.metersToInches(navX.getDisplacementX());
            prevDY = Units.metersToInches(navX.getDisplacementY());
            navX.resetDisplacement();
        }
        Vector v = Vector.fromXY(navX.getDisplacementX() + prevDX, navX.getDisplacementY() + prevDY);
        
        //rotate the xy position to match the same 0 as the encoder vector
        v.theta += Math.toRadians(prevAng);
        
        return v;
    }

    public void resetAng(){
        prevAng = navX.getYaw();
    }

    public void resetPos(){
        prevDX = 0;
        prevDY = 0;
        navX.resetDisplacement();
    }

    public void overrideAng(double angle){
        prevAng = angle;
        navX.zeroYaw();
    }
}
