package frc.robot.Auton.AutoSubsytem.ManualCommands;

import frc.robot.RobotContainer;
import frc.robot.Auton.AutoSubsytem.AutonDriveAbsolute;
import frc.robot.Auton.AutoSubsytem.MotionProfiling.MPAutonDriveAbsolute;
import frc.robot.Auton.AutonSequential.PositionProvider;

public class ManAutonAbsDrGthSht extends ManAutonRelDrGthSht{

    public ManAutonAbsDrGthSht(RobotContainer r, PositionProvider p, int idx, boolean gather, boolean shoot){
        super(r, p, idx, gather, shoot);
    }

    public ManAutonAbsDrGthSht(RobotContainer r, PositionProvider p, int idx){
        super(r, p, idx);
    }

    @Override
    protected AutonDriveAbsolute getDrive(){
        return new MPAutonDriveAbsolute(r, p, idx);
    }
}
