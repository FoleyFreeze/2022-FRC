package frc.robot.Sensors;

import java.util.concurrent.ConcurrentLinkedQueue;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Util.Vector;

public class Vision {

    ConcurrentLinkedQueue<VisionData> visionQueue;
    
    public static class VisionData{
        public Vector location;
        public double timestamp;
        public Type type;
    }
    public enum Type {
        RED_CARGO, BLUE_CARGO, VISION_TARGET
    }

    //vision packet from the pi
    // id, currTime, calcTime, {dist, angle, color} repeat

    NetworkTable piTable;
    public CalsSensors cals;

    public Vision(CalsSensors cals){
        this.cals = cals;
        visionQueue = new ConcurrentLinkedQueue<>();

        addListener();
    }

    int prevIdCargo;
    int prevIdTarget;
    int droppedCargoFrames;
    int droppedTargetFrames;
    double lastCargoTime;
    double lastTargetTime;
    double dtLimit = 0.5;
    double lastCargoDt;
    double lastTargetDt;

    public void addListener(){
        NetworkTableInstance.getDefault().setUpdateRate(0.01);
        piTable = NetworkTableInstance.getDefault().getTable("pi");

        piTable.addEntryListener("Cargo", (table, key, entry, value, flags) -> {
            try{
                double now = Timer.getFPGATimestamp();

                VisionData data = new VisionData();

                // id, currTime, calcTime, {dist, angle, color} repeat
                String[] parts = value.getString().split(",");
                
                int id = Integer.parseInt(parts[0]);
                int dId = id - prevIdCargo;
                if(prevIdCargo != 0 && dId > 0){
                    droppedCargoFrames += dId - 1;
                }
                prevIdCargo = id;

                double t = now;
                double dt = t - lastCargoTime;
                lastCargoTime = t;
                if(dt < dtLimit){
                    lastCargoDt = dt;
                }

                double currTime = Double.parseDouble(parts[1]);
                double calcTime = Double.parseDouble(parts[2]);

                //gets image start time
                data.timestamp = now - (((now - currTime) / 2) + 0.5 * calcTime);

                double x = Double.parseDouble(parts[3]);
                //note that positive angles for the pi are to the right
                //even though they are to the left for us
                double angle = Double.parseDouble(parts[4]);
                
                data.location = Vector.fromXT(x, Math.toRadians(angle));
                data.location.theta += Math.toRadians(cals.ballCamAngleL); //camera faces "x"+

                data.location.add(cals.ballCamLocationL);

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
                int dId = id - prevIdTarget;
                if(prevIdTarget != 0 && dId > 0){
                    droppedTargetFrames += dId - 1;
                }
                prevIdTarget = id;

                double t = now;
                double dt = t - lastTargetTime;
                lastTargetTime = t;
                if(dt < dtLimit){
                    lastTargetDt = dt;
                }

                double currTime = Double.parseDouble(parts[1]);
                double calcTime = Double.parseDouble(parts[2]);

                //gets image start time
                data.timestamp = now - (((now - currTime) / 2) + 0.5 * calcTime);

                double x = Double.parseDouble(parts[3]);
                double angle = Double.parseDouble(parts[4]);

                data.location = Vector.fromXT(x, Math.toRadians(angle));
                data.location.theta += Math.toRadians(cals.tgtCamAngle); //shooter faces "x"-

                data.location.add(cals.tgtCamLocation);

                data.type = Type.VISION_TARGET;

                visionQueue.add(data);
            }catch(Exception e){
                e.printStackTrace();
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate | EntryListenerFlags.kImmediate);
    }

    public void periodic(){
        piTable.getEntry("RobotTime").setDouble(Timer.getFPGATimestamp());
        piTable.getEntry("RedAlliance").setBoolean(DriverStation.getAlliance().equals(Alliance.Red));

        SmartDashboard.putNumber("Dropped Frames Cargo", droppedCargoFrames);
        SmartDashboard.putNumber("Dropped Frames Target", droppedTargetFrames);
        SmartDashboard.putNumber("Cargo dt", lastCargoDt);
        SmartDashboard.putNumber("Target dt", lastTargetDt);
    }
}
