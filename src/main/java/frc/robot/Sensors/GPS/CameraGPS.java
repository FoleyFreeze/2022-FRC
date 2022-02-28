package frc.robot.Sensors.GPS;

import frc.robot.Sensors.Vision.VisionData;
import frc.robot.Util.Vector;

public class CameraGPS implements AutoCloseable{
    
    public int head = -1;

    public static class Location{
        public Vector pos;
        public double angle;
        public double timestamp;
        public double cannonAng;//in degrees
    }
    public Location[] locationHistory;

    public CameraGPS(int historySize){
        locationHistory = new Location[historySize];
        for(int i = 0; i < historySize; i++) {
            locationHistory[i] = new Location();
        }
    }

    public void addLocation(Vector pos, double ang, double time, double cannonAng){
        head++;
        if(head > locationHistory.length - 1){
            head = 0;
        }

        locationHistory[head].pos = pos;
        locationHistory[head].angle = ang;
        locationHistory[head].timestamp = time;
        locationHistory[head].cannonAng = cannonAng;
    }

    public Location interpolate(double time){
        int i = head;
        while(locationHistory[i].timestamp > time){
            i--;
            if(i < 0) i = locationHistory.length-1;
        }
        Location before = locationHistory[i];
        i++;
        if(i >= locationHistory.length) i = 0;
        Location after = locationHistory[i];

        double dt = after.timestamp - before.timestamp;
        double slopeX = (after.pos.getX() - before.pos.getX()) / dt;
        double slopeY = (after.pos.getY() - before.pos.getY()) / dt;
        double slopeAngle = (after.angle - before.angle) / dt;

        Location interp = new Location();
        double axisDiff = time - before.timestamp;
        interp.pos = Vector.fromXY(before.pos.getX() + (axisDiff * slopeX), before.pos.getY() + (axisDiff * slopeY));
        interp.angle = before.angle + (axisDiff * slopeAngle);
        return interp;
    }

    //update only locations from after image was taken
    public void updateArray(Vector error, double time, double cannonAng){
        int i = head;
        while(locationHistory[i].timestamp > time){
            locationHistory[i].pos.add(error);
            locationHistory[i].cannonAng = cannonAng;
            i--;
            if(i < 0){
                i = locationHistory.length;
            }
        }
    }

    //updates all values if they are not null
    public void updateArray(Vector error, double cannonAng){
        for(int i = 0; i > locationHistory.length; i++){
            if(locationHistory[i].pos != null){
                locationHistory[i].pos.add(error);
                locationHistory[i].cannonAng = cannonAng;
            }
        }
    }

    public Vector imgToLocation(VisionData img){
        Location botLoc = interpolate(img.timestamp);
        img.location.theta += botLoc.angle;

        return img.location.add(botLoc.pos);
    }
    
    @Override
    public void close() throws Exception {
        
    }
}
