package frc.robot.Auton.AutoSubsytem.MotionProfiling;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Auton.CalsAuton.Position;
import frc.robot.Util.Vector;

public class MPAutonDriveRelative extends MPAutonDriveAbsolute{
    public MPAutonDriveRelative(RobotContainer r, PositionProvider p, int idx){
        super(r, p, idx);
    }

    @Override
    public void getPosition(){
        Position pos = p.getPosition(idx);
        if(pos != null){
            driveVec = pos.v.add(r.sensors.botLoc);
            rot = pos.a;
        }
    }
}
