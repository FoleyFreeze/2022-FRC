package frc.robot.Sensors.Utilities;

import com.kauailabs.navx.frc.AHRS;

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
            prevDX = navX.getDisplacementX();
            prevDY = navX.getDisplacementY();
            navX.resetDisplacement();
        }
        return Vector.fromXY(navX.getDisplacementX() + prevDX, navX.getDisplacementY() + prevDY);
    }

    public void resetAng(){
        prevAng = navX.getYaw();
    }

    public void overrideAng(double angle){
        prevAng = angle;
        navX.zeroYaw();
    }
}
