import frc.robot.Drive.Wheel;
import frc.robot.Util.Vector;

import static org.junit.Assert.*;
import org.junit.*;

public class SwerveMathTest {
    public static final double DELTA = 1e-2;
    Wheel wheel;

    @Before
    public void setup(){
        wheel = new Wheel();
    }

    @After
    public void shutdown(){

    }

    @Test
    public void testVecAdd(){
        Vector v1 = Vector.fromXY(1, 1);
        Vector v2 = Vector.fromXY(-1, 2);

        Vector out = Vector.addVectors(v1, v2);
        assertEquals(1 + -1, out.getX(), DELTA);
        assertEquals(1+2, out.getY(), DELTA);
    }

    @Test
    public void testVecSub(){
        Vector v1 = Vector.fromXY(1, -1);
        Vector v2 = Vector.fromXY(0, 0);

        Vector out = Vector.subVectors(v1, v2);
        assertEquals(1 - 0, out.getX(), DELTA);
        assertEquals(-1 - 0, out.getY(), DELTA);
    }

    @Test public void testCalcRotAngle(){
        wheel.wheelLocation = Vector.fromXY(-1, -1);
        double val = wheel.calcRotAngle(new Vector(0, 0));
        //System.out.format("%.3f matches %.3f\n",Math.toDegrees(val), Math.toDegrees(0));
        assertEquals(-Math.PI / 4, val, DELTA);
    }
}
