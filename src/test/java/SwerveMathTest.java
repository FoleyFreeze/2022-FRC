import frc.robot.Drive.CalsDrive;
import frc.robot.Drive.CmdDrive;
import frc.robot.Drive.Wheel;
import frc.robot.Util.Vector;

import static org.junit.Assert.*;
import org.junit.*;

public class SwerveMathTest {
    public static final double DELTA = 1e-2;
    Wheel wheel;

    @Before
    public void setup(){
        wheel = new Wheel((new CalsDrive()).FRwheel);
    }

    @After
    public void shutdown(){
        try{
            wheel.close();
        } catch (Exception e){
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
    public void testAdd(){
        Vector v1 = Vector.fromXY(2, -1);
        Vector v2 = Vector.fromXY(1, 2);

        Vector out = v1.add(v2);

        assertEquals(3, out.getX(), DELTA);
        assertEquals(1, out.getY(), DELTA);
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
        Vector v = Vector.fromXY(0.5, 0.5);
        CmdDrive.mapSquareToCircle(v);
        System.out.println(v.r);
        assertEquals(0.5, v.r, DELTA);
    }
}
