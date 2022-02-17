import org.junit.*;

import frc.robot.RobotContainer;

public class CannonTest {
    double DELTA = 1e-2;

    RobotContainer r;

    @Before
    public void setup(){
        r = new RobotContainer();
    }

    @After
    public void shutdown(){
        try{
            r.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
