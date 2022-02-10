import static org.junit.Assert.assertEquals;
import org.junit.*;

import frc.robot.RobotContainer;
import frc.robot.Drive.SysDriveTrain;
import frc.robot.Sensors.GPS.CameraGPS;
import frc.robot.Sensors.GPS.SwerveEncoder;
import frc.robot.Util.Vector;

public class SensorsTest {

    RobotContainer rc;
    CameraGPS gps;
    SwerveEncoder encoder;
    SysDriveTrain drive;

    double DELTA = 1e-2;

    @Before
    public void setup(){
        rc = new RobotContainer();
        drive = rc.drive;
        gps = rc.sensors.camera;
        encoder = rc.sensors.encoders;
    }

    @After
    public void shutdown(){
        try{
            rc.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testInterp(){
        gps.addLocation(Vector.fromXY(0.5, 0.5), 0, 0.5);
        gps.addLocation(Vector.fromXY(0.6, 0.8), 0.4, 0.6);

        //Location l = gps.interpolate(0.52);
    }

    @Test
    public void testCamera(){
        gps.addLocation(Vector.fromXY(0.1, 0.7), 2, 100);
        gps.addLocation(new Vector(1, 2), 1, 110);

        System.out.println(gps.locationHistory[0].pos.getX());
        System.out.println(gps.locationHistory[0].angle);
        System.out.println(gps.locationHistory[0].timestamp);

        assertEquals(0.1, gps.locationHistory[0].pos.getX(), DELTA);
        assertEquals(0.7, gps.locationHistory[0].pos.getY(), DELTA);
        assertEquals(2, gps.locationHistory[0].angle, DELTA);
    }

    @Test
    public void testEncoders(){
        double pi = Math.PI;
        Vector[] wheelVecs = {new Vector(1, pi/4), new Vector(1, -pi/4), new Vector(1, (3*pi)/4), new Vector(1, -(3*pi)/4)};

        Vector averageTrans = encoder.averageTranslation(wheelVecs);
        double averageRot = encoder.averageRotation(wheelVecs, averageTrans);

        System.out.println(averageRot);
    }
}
