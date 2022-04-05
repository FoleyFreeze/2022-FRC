package frc.robot.Auton.AutoSubsytem.ManualCommands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutoSubsytem.AutonDriveAbsolute;
import frc.robot.Auton.AutoSubsytem.AutonDriveRelative;
import frc.robot.Auton.AutoSubsytem.CameraCommands.AutonShoot;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Intake.CmdAutoGather;

public class ManAutonRelDrGthSht extends SequentialCommandGroup{

    RobotContainer r;
    PositionProvider p;
    int idx;

    public ManAutonRelDrGthSht(RobotContainer r, PositionProvider p, int idx, boolean gather, boolean shoot){
        this.r = r;
        this.p = p;
        this.idx = idx;
        if(gather){
            addCommands(new ParallelRaceGroup(getDrive(), new CmdAutoGather(r)));
        } else {
            addCommands(getDrive());
        }
        if(shoot) addCommands(new WaitCommand(0.2), new AutonShoot(r));
    }

    public ManAutonRelDrGthSht(RobotContainer r, PositionProvider p, int idx){
        this(r, p, idx, true, true);
    }   

    protected AutonDriveAbsolute getDrive(){
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
