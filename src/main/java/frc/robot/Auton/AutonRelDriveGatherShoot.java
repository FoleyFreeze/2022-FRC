package frc.robot.Auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutoSubsytem.AutonDriveAbsolute;
import frc.robot.Auton.AutoSubsytem.AutonDriveRelative;
import frc.robot.Auton.AutoSubsytem.AutonGather;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Cannon.CmdShoot;

public class AutonRelDriveGatherShoot extends SequentialCommandGroup{

    PositionProvider p;
    int idx;

    public AutonRelDriveGatherShoot(RobotContainer r, PositionProvider p, int idx){
        this(r,p,idx,true, true);
    }

    public AutonRelDriveGatherShoot(RobotContainer r, PositionProvider p, int idx, boolean gather, boolean shoot){
        this.p = p;
        this.idx = idx;
        addCommands(getDrive(r, p, idx));
        if(gather) addCommands(new AutonGather(r));
        if(shoot) addCommands(new CmdShoot(r));  
    }

    protected AutonDriveAbsolute getDrive(RobotContainer r, PositionProvider p, int idx){
        return new AutonDriveRelative(r, p, idx);
    }

    @Override
    public void execute(){
        if(!p.getSkip(idx)) super.execute();
    }

    @Override
    public boolean isFinished(){
        return p.getSkip(idx) || super.isFinished();
    }
}
