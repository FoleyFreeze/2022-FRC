package frc.robot.Auton.AutoSubsytem.ManualCommands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Intake.CmdAutoGather;

public class ManAutonGatherOnly extends ParallelRaceGroup {
    
    RobotContainer r;
    PositionProvider p;
    int idx;

    public ManAutonGatherOnly(RobotContainer r, PositionProvider p, int idx){
        this.r = r;
        this.p = p;
        this.idx = idx;
        addCommands(new WaitCommand(1 + 0.25), new CmdAutoGather(r));
    }
    
    @Override
    public void initialize() {
        super.initialize();
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
