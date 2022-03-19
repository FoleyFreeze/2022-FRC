package frc.robot.Auton.AutoSubsytem.CameraCommands;

import frc.robot.RobotContainer;
import frc.robot.Auton.AutoSubsytem.AutonDriveAbsolute;
import frc.robot.Auton.AutonSequential.PositionProvider;

public class AutonAbsDrvGthSht extends AutonRelDrvGthSht{
    
    public AutonAbsDrvGthSht(RobotContainer r, PositionProvider p, int idx, boolean gather, boolean shoot){
        super(r,p,idx,true,true);
    }

    public AutonAbsDrvGthSht(RobotContainer r, PositionProvider p, int idx){
        super(r,p,idx);
    }

    @Override
    protected AutonDriveAbsolute getDrive(RobotContainer r, PositionProvider p, int idx){
        return new AutonDriveAbsolute(r, p, idx);
    }
}
