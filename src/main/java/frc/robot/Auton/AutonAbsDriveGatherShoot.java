package frc.robot.Auton;

import frc.robot.RobotContainer;
import frc.robot.Auton.AutonSequential.PositionProvider;

public class AutonAbsDriveGatherShoot extends AutonRelDriveGatherShoot{
    
    public AutonAbsDriveGatherShoot(RobotContainer r, PositionProvider p, int idx){
        super(r,p,idx);
    }

    @Override
    protected AutoDriveAbsolute getDrive(RobotContainer r, PositionProvider p, int idx){
        return new AutoDriveAbsolute(r, p, idx);
    }
}
