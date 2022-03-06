package frc.robot.Auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Cannon.CmdShoot;

public class AutonRelDriveGatherShoot extends SequentialCommandGroup{

    PositionProvider p;
    int idx;

    public AutonRelDriveGatherShoot(RobotContainer r, PositionProvider p, int idx){
        this(r,p,idx,true);
    }

    public AutonRelDriveGatherShoot(RobotContainer r, PositionProvider p, int idx, boolean gather){
        this.p = p;
        this.idx = idx;
        addCommands(getDrive(r, p, idx));
        if(gather) addCommands(new AutonGather(r));
        addCommands(new CmdShoot(r));  
    }

    protected AutoDriveAbsolute getDrive(RobotContainer r, PositionProvider p, int idx){
        return new AutoDriveRelative(r, p, idx);
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
