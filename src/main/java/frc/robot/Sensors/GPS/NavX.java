package frc.robot.Sensors.GPS;

import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.frc.AHRS.SerialDataType;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.SerialPort;
import frc.robot.Util.Vector;

public class NavX {

    public AHRS navX;
    public double prevDX;
    public double prevDY;
    public double prevAng;

    public NavX(){
        //navX = new AHRS(Port.kUSB);
        navX = new AHRS(SerialPort.Port.kUSB, SerialDataType.kProcessedData, (byte)200);
        navX.calibrate();
        navX.zeroYaw();
    }
    
    public double getFieldOrientAngle(){
        return -navX.getYaw() + prevAng;
    }

    public Vector getFieldOrientDisplacement(boolean isMoving){
        double x = Units.metersToInches(navX.getDisplacementX());
        double y = Units.metersToInches(navX.getDisplacementY());

        if(!isMoving){
            prevDX = x;
            prevDY = y;
            navX.resetDisplacement();
        }
        
        Vector v = Vector.fromXY(x + prevDX, y + prevDY);
        
        //rotate the xy position to match the same 0 as the encoder vector
        v.theta += Math.toRadians(prevAng) + Math.PI/2;
        
        return v;
    }

    public void resetAng(){
        //prevAng = navX.getYaw();
        prevAng = 0;
        navX.zeroYaw();
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
