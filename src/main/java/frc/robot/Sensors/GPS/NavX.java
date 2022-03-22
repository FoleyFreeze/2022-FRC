package frc.robot.Sensors.GPS;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;

public class NavX implements AutoCloseable {

    public AHRS navX;
    public double prevDX;
    public double prevDY;
    public double prevAng;

    public boolean isDisabled = false;

    public NavX(){
        if(isDisabled) return;

        //navX = new AHRS(SerialPort.Port.kUSB, SerialDataType.kProcessedData, (byte)50);
        navX = new AHRS(SPI.Port.kMXP);

        if(navX.isConnected()){
            navX.calibrate();
            navX.zeroYaw();
        }
    }

    public double getFieldOrientAngle(){
        if(isDisabled) return 0;

        SmartDashboard.putBoolean("NavX connected", navX.isConnected());
        return Angle.normDeg(-navX.getYaw() + prevAng);
        /*
        if(navX.isConnected()){
            return -navX.getYaw() + prevAng;
        } else {
            return 0;
        }*/
    }

    public Vector getFieldOrientDisplacement(boolean isMoving){
        if(isDisabled) return new Vector(0,0);

        if(navX.isConnected()){
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
        } else {
            return new Vector(0,0);
        }
    }

    public void resetAng(){
        if(isDisabled) return;

        if(navX.isConnected()){
            //prevAng = navX.getYaw();
            prevAng = 0;
            navX.zeroYaw();
        } else {
            return;
        }
    }

    public void resetPos(){
        if(isDisabled) return;

        if(navX.isConnected()){
            prevDX = 0;
            prevDY = 0;
            navX.resetDisplacement();
        } else {
            return;
        }
    }

    public void overrideAng(double angle){
        if(isDisabled) return;

        if(navX.isConnected()){
            prevAng = angle;
            navX.zeroYaw();
        } else {
            return;
        }
    }

    @Override
    public void close() throws Exception {
        navX.close();
    }
}
