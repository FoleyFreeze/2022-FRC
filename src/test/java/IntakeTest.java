import org.junit.*;

import frc.robot.RobotContainer;
import frc.robot.Intake.SysIntake;
import frc.robot.Util.Vector;

public class IntakeTest {
    double DELTA = 1e-2;

    RobotContainer r;
    SysIntake intake;

    @Before
    public void setup(){
        r = new RobotContainer();
        intake = r.intake;
    }

    @After
    public void shutdown(){
        try{
            r.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testCamerDrive(){
                                          //cargo location,                     robot location
        Vector cargoPos = Vector.subVectors(new Vector(1, Math.toRadians(-90)), new Vector(1.41, Math.toRadians(-45)));
        cargoPos.theta -= Math.toRadians(-45);
        
        double zR = intake.cals.kR * cargoPos.theta;
        double x = intake.cals.kX * cargoPos.getX();
        double y = Math.max(intake.cals.yPower - x - zR, 0);
        Vector xy = Vector.fromXY(x, y);

        System.out.println("DriveVec: " + xy.toStringXY());
        System.out.println("AngError: " + Math.toDegrees(zR));
    }
}
