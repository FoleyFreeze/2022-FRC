package frc.robot.Auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutoSubsytem.AutonDriveAbsolute;
import frc.robot.Auton.AutoSubsytem.AutonDriveRelative;
import frc.robot.Auton.AutoSubsytem.AutonGather;
import frc.robot.Auton.AutoSubsytem.AutonShoot;
import frc.robot.Auton.AutonSequential.PositionProvider;

public class AutonRelDrvGthrShoot extends SequentialCommandGroup{

    PositionProvider p;
    int idx;

    public AutonRelDrvGthrShoot(RobotContainer r, PositionProvider p, int idx){
        this(r,p,idx,true, true);
    }

    public AutonRelDrvGthrShoot(RobotContainer r, PositionProvider p, int idx, boolean gather, boolean shoot){
        this.p = p;
        this.idx = idx;
        addCommands(getDrive(r, p, idx));
        if(gather) addCommands(new AutonGather(r));
        if(shoot) addCommands(new AutonShoot(r));
    }

    protected AutonDriveAbsolute getDrive(RobotContainer r, PositionProvider p, int idx){
        return new AutonDriveRelative(r, p, idx);
    }

    @Override
    public void initialize(){
        super.initialize();
        System.out.println("Auton " + idx);
    }

    @Override
    public void execute(){
        if(p.todoList(idx)) super.execute();
    }

    @Override
    public boolean isFinished(){
        return !p.todoList(idx) || super.isFinished();
    }
}
