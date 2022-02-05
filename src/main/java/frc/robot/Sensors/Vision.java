package frc.robot.Sensors;

import java.util.concurrent.ConcurrentLinkedQueue;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Util.Vector;

public class Vision {

    ConcurrentLinkedQueue<VisionData> visionQueue;
    
    public static class VisionData{
        public Vector botRelativeLoc;
        public double timestamp;
        public Type type;
    }
    public enum Type {
        RED_CARGO, BLUE_CARGO, VISION_TARGET
    }

    //vision packet from the pi
    // id, currTime, calcTime, {dist, angle, color} repeat

    NetworkTable piTable;

    public Vision(){
        visionQueue = new ConcurrentLinkedQueue<>();
    }

    public void addListener(){
        piTable = NetworkTableInstance.getDefault().getTable("pi");

        piTable.addEntryListener("Cargo", (table, key, entry, value, flags) -> {
            try{
                double now = Timer.getFPGATimestamp();

                VisionData data = new VisionData();

                // id, currTime, calcTime, {dist, angle, color} repeat
                String[] parts = value.getString().split(",");
                
                int id = Integer.parseInt(parts[0]);

                double currTime = Double.parseDouble(parts[1]);
                double calcTime = Double.parseDouble(parts[2]);

                //gets image start time
                data.timestamp = now - (((now - currTime) / 2) + 0.5 * calcTime);

                double dist = Double.parseDouble(parts[3]);
                double angle = Double.parseDouble(parts[4]);

                data.botRelativeLoc = new Vector(dist, angle);

                if(Integer.parseInt(parts[5]) == 1){
                    data.type = Type.BLUE_CARGO;//blue is true!
                } else {
                    data.type = Type.RED_CARGO;//red is dead!
                }

                visionQueue.add(data);
            }catch(Exception e){
                e.printStackTrace();
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate | EntryListenerFlags.kImmediate);

        piTable.addEntryListener("Target", (table, key, entry, value, flags) -> {
            try{
                double now = Timer.getFPGATimestamp();
                VisionData data = new VisionData();

                // id, currTime, calcTime, {dist, angle, color} repeat
                String[] parts = value.getString().split(",");

                int id = Integer.parseInt(parts[0]);

                double currTime = Double.parseDouble(parts[1]);
                double calcTime = Double.parseDouble(parts[2]);

                //gets image start time
                data.timestamp = now - (((now - currTime) / 2) + 0.5 * calcTime);

                double dist = Double.parseDouble(parts[3]);
                double angle = Double.parseDouble(parts[4]);

                data.botRelativeLoc = new Vector(dist, angle);

                data.type = Type.VISION_TARGET;

                visionQueue.add(data);
            }catch(Exception e){
                e.printStackTrace();
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate | EntryListenerFlags.kImmediate);
    }
}
