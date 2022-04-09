package frc.robot.Auton.AutoSubsytem;

import frc.robot.RobotContainer;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Auton.CalsAuton.Position;

public class AutonDriveRelative extends AutonDriveAbsolute{

    public AutonDriveRelative(RobotContainer r, PositionProvider p, int idx) {
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
