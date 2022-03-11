package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Auton.CalsAuton.Position;

public class AutonDriveRelative extends AutonDriveAbsolute{

    public AutonDriveRelative(RobotContainer r, PositionProvider p, int idx) {
        super(r, p, idx);
    }

    @Override
    public void initialize(){
        Position pos = p.getPosition(idx);
        if(pos != null){
            driveVec = pos.v.add(r.sensors.botLoc);
            rot = pos.a;
        }

        starttime = Timer.getFPGATimestamp();
    }
}
