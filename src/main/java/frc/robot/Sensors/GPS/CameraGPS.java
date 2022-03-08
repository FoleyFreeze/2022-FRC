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
    }

    public void addLocation(Vector pos, double ang, double time, double cannonAng){
        head++;
        if(head > locationHistory.length - 1){
            head = 0;
        }

        if(locationHistory[head] == null) {
            locationHistory[head] = new Location();
            locationHistory[head].pos = new Vector(0,0);
        }

        locationHistory[head].pos.r = pos.r;
        locationHistory[head].pos.theta = pos.theta;
        locationHistory[head].angle = ang;
        locationHistory[head].timestamp = time;
        locationHistory[head].cannonAng = cannonAng;
    }

    public Location interpolate(double time){
        if(head < 0) return null;

        int i = head;
        while(locationHistory[i] != null && locationHistory[i].timestamp > time){
            i--;
            if(i < 0) i = locationHistory.length-1;
            if(i == head) {
                //if the time is so old its off the map, just use the oldest location
                i = head - 1;
                if(head < 0) head = locationHistory.length - 1;
                break;
            }
        }

        Location before = locationHistory[i];
        i++;
        if(i >= locationHistory.length) i = 0;
        Location after = locationHistory[i];

        if(before == null || after == null) return null;

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
            if(locationHistory != null && locationHistory[i].pos != null){
                locationHistory[i].pos.add(error);
                locationHistory[i].cannonAng = cannonAng;
            }
        }
    }

    public Vector imgToLocation(VisionData img){
        Location botLoc = interpolate(img.timestamp);
        if(botLoc != null){
            img.location.theta += botLoc.angle;
            img.location.add(botLoc.pos);
        }

        return img.location;
    }
    
    @Override
    public void close() throws Exception {
        
    }
}
