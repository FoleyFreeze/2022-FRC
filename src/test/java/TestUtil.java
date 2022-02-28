import static org.junit.Assert.assertEquals;
import org.junit.*;

import frc.robot.Util.Interpolate;

public class TestUtil {
    public static final double DELTA = 1e-2;
    public Interpolate interpolate;

    @Before
    public void setup(){
        interpolate = new Interpolate();
    }

    @After
    public void shutdown(){
        interpolate.close();
    }

    @Test
    public void testInterp(){
        double[] axis = {1, 2, 3, 4};
        double[] table = {0.5, 1.0, 1.5, 2.0};

        double value = 1.25;
        double result = Interpolate.interpolate(axis, table, value);

        assertEquals(0.625, result, DELTA);
    }
}
