import frc.robot.RobotContainer;
import frc.robot.Drive.CalsDrive;
import frc.robot.Drive.CmdDrive;
import frc.robot.Drive.SysDriveTrain;
import frc.robot.Drive.Wheel;
import frc.robot.Inputs.CalsInputs;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;

import static org.junit.Assert.*;
import org.junit.*;

public class SwerveMathTest {
    public static final double DELTA = 1e-2;
    Wheel wheel;
    Inputs inputs;
    SysDriveTrain drive;

    @Before
    public void setup(){
        inputs = new Inputs(new CalsInputs());
        drive = new SysDriveTrain(new CalsDrive());
        wheel = drive.wheels[0];
    }

    @After
    public void shutdown(){
        try{
            inputs.close();
            drive.close();
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
        double angleDiff = 0;
        double magnitude = 1;
        /*
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

        //System.out.println(out.getY());
        assertEquals(1, out.getX(), DELTA);
        assertEquals(-3, out.getY(), DELTA);
    }

    @Test 
    public void testCalcRotAngle(){
        wheel.wheelLocation = Vector.fromXY(-1, -1);
        double val = wheel.calcRotAngle(new Vector(0, 0));
        //System.out.format("%.3f matches %.3f\n",Math.toDegrees(val), Math.toDegrees(0));
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
    public void testSwerveFunc(){
        drive.driveSwerve(Vector.fromXY(0, -1), 0);

        for(Wheel w : drive.wheels){
            System.out.println(w.cals.name + " " + w.driveVec.toStringPolar());
        }

        System.out.println("Joystick Y " + inputs.getDriveY());
        System.out.println("Joystick X " + inputs.getDriveX());
        System.out.println("Joystick R " + inputs.getDrivezR());
    }

    @Test
    public void testAngleNorm(){
        double a = 179;
        double b = Angle.normDeg(a);
        assertEquals(179, b, DELTA);
        a = 181;
        b = Angle.normDeg(a);
        assertEquals(-179, b, DELTA);
        a = 361;
        b = Angle.normDeg(a);
        assertEquals(1, b, DELTA);
        a = 719;
        b = Angle.normDeg(a);
        assertEquals(-1, b, DELTA);

        a = -179;
        b = Angle.normDeg(a);
        assertEquals(-179, b, DELTA);
        a = -181;
        b = Angle.normDeg(a);
        assertEquals(179, b, DELTA);
        a = -361;
        b = Angle.normDeg(a);
        assertEquals(-1, b, DELTA);
        a = -719;
        b = Angle.normDeg(a);
        assertEquals(1, b, DELTA);
    }

    @Test
    public void testFromXY(){
        Vector v = Vector.fromXY(0, -1);
        assertEquals(1, v.r, DELTA);
        assertEquals(-Math.PI/2, v.theta, DELTA);
    }
}
