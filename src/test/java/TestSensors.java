import static org.junit.Assert.*;
import org.junit.*;

import frc.robot.Sensors.Utilities.CameraGPS;
import frc.robot.Sensors.Utilities.CameraGPS.Location;
import frc.robot.Util.Vector;

public class TestSensors {
    CameraGPS gps;

    @Before
    public void setup(){
        gps = new CameraGPS(10);
    }

    @Before
    public void shutdown(){
        try{
            gps.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testInterp(){
        gps.addLocation(Vector.fromXY(0.5, 0.5), 0, 0.5);
        gps.addLocation(Vector.fromXY(0.6, 0.8), 0.4, 0.6);

        Location l = gps.interpolate(0.52);
        System.out.println("X: " + l.pos.getX() + "   Y: " + l.pos.getY() + "   Angle: " + l.angle);
    }
}
