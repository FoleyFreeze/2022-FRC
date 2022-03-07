package frc.robot.Auton;

import frc.robot.RobotContainer;
import frc.robot.Auton.AutoSubsytem.AutonDriveAbsolute;
import frc.robot.Auton.AutonSequential.PositionProvider;

public class AutonAbsDriveGatherShoot extends AutonRelDriveGatherShoot{
    
    public AutonAbsDriveGatherShoot(RobotContainer r, PositionProvider p, int idx, boolean gather, boolean shoot){
        super(r,p,idx,true,true);
    }

    public AutonAbsDriveGatherShoot(RobotContainer r, PositionProvider p, int idx){
        super(r,p,idx);
    }

    @Override
    protected AutonDriveAbsolute getDrive(RobotContainer r, PositionProvider p, int idx){
        return new AutonDriveAbsolute(r, p, idx);
    }
}
