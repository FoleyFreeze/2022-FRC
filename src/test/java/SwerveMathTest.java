import frc.robot.RobotContainer;
import frc.robot.Drive.CalsDrive;
import frc.robot.Drive.CmdDrive;
import frc.robot.Drive.SysDriveTrain;
import frc.robot.Drive.Wheel;
import frc.robot.Inputs.CalsInputs;
import frc.robot.Inputs.Inputs;
import frc.robot.Sensors.CalsSensors;
import frc.robot.Sensors.Sensors;
import frc.robot.Sensors.GPS.CameraGPS;
import frc.robot.Sensors.GPS.SwerveEncoder;
import frc.robot.Util.Vector;

import static org.junit.Assert.*;
import org.junit.*;

public class SwerveMathTest {
    public static final double DELTA = 1e-2;
    Wheel wheel;
    Inputs inputs;
    Sensors sensors;
    SysDriveTrain drive;
    SwerveEncoder encoder;
    CameraGPS gps;
    RobotContainer c;

    @Before
    public void setup(){
        c = new RobotContainer();
        inputs = new Inputs(new CalsInputs());
        sensors = new Sensors(new CalsSensors(), c);
        drive = new SysDriveTrain(new CalsDrive(), c);
        wheel = drive.wheels[0];
        gps = new CameraGPS(10);
        encoder = new SwerveEncoder(drive.wheels);
    }

    @After
    public void shutdown(){
        try{
            inputs.close();
            drive.close();
            gps.close();
            encoder.close();
        } catch (Exception e){
            System.out.println("Exception during close:");
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    @Test
    public void testStaticVecAdd(){
        Vector v1 = Vector.fromXY(1, 1);
        Vector v2 = Vector.fromXY(-1, 2);

        Vector out = Vector.addVectors(v1, v2);
        assertEquals(1 + -1, out.getX(), DELTA);
        assertEquals(1+2, out.getY(), DELTA);
    }

    @Test
    public void testStaticVecSub(){
        Vector v1 = Vector.fromXY(1, -1);
        Vector v2 = Vector.fromXY(0, 0);

        Vector out = Vector.subVectors(v1, v2);
        assertEquals(1 - 0, out.getX(), DELTA);
        assertEquals(-1 - 0, out.getY(), DELTA);
    }

    @Test
    public void testDriveMag(){
        /*
        double angleDiff = 0;
        double magnitude = 1;

        for(int i = 0; i < 360; i++){
            angleDiff++;
            if(Math.abs(angleDiff) > 90){
                magnitude = -magnitude;
                if(angleDiff > 0){
                    angleDiff -= 180;
                } else {
                    angleDiff += 180;
                }
            }
            System.out.println("angleDiff: " + angleDiff);
            System.out.println("magnitude: " + magnitude);
        }
        */
    }

    @Test
    public void testSub(){
        Vector v1 = Vector.fromXY(2, -1);
        Vector v2 = Vector.fromXY(1, 2);

        Vector out = v1.subtract(v2);

        assertEquals(1, out.getX(), DELTA);
        assertEquals(-3, out.getY(), DELTA);
    }

    @Test 
    public void testCalcRotAngle(){
        wheel.wheelLocation = Vector.fromXY(-1, -1);
        double val = wheel.calcRotAngle(new Vector(0, 0));
        assertEquals(-Math.PI / 4, val, DELTA);
    }

    @Test
    public void testSquToCircle(){
        Vector v = Vector.fromXY(0, -1);
        CmdDrive.mapSquareToCircle(v);
        System.out.println(v.toStringPolar());
        assertEquals(1, v.r, DELTA);
        assertEquals(-90, Math.toDegrees(v.theta), DELTA);
    }

    @Test
    public void testDeadBand(){
        double v1 = inputs.expo(-2, 4);
        assertEquals(-16, v1, DELTA);
        double v2 = inputs.deadBand(0.1, 0.1, 0.9, 0.03);
        assertEquals(0.03, v2, DELTA);
        v2 = inputs.deadBand(0.9, 0.1, 0.9, 0.03);
        assertEquals(1, v2, DELTA);
        v2 = inputs.deadBand(0.5, 0.1, 0.9, 0.03);
        assertEquals(0.515, v2, DELTA);
    }

    @Test
    public void testFieldOrient(){
        drive.driveSwerve(Vector.fromXY(1, 1), 0.5);
    }



    //sensors testing

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

      /*System.out.println(gps.locationHistory[0].pos.getX());
        System.out.println(gps.locationHistory[0].angle);
        System.out.println(gps.locationHistory[0].timestamp);*/

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
