package frc.robot.Auton.AutoSubsytem.ManualCommands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutoSubsytem.AutonDriveAbsolute;
import frc.robot.Auton.AutoSubsytem.AutonDriveRelative;
import frc.robot.Auton.AutoSubsytem.CameraCommands.AutonShoot;
import frc.robot.Auton.AutoSubsytem.MotionProfiling.MPAutonDriveRelative;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Intake.CmdAutoGather;

public class ManAutonRelDrGthSht extends SequentialCommandGroup{

    RobotContainer r;
    PositionProvider p;
    int idx;

    public ManAutonRelDrGthSht(RobotContainer r, PositionProvider p, int idx, boolean gather, double shootDist){
        this.r = r;
        this.p = p;
        this.idx = idx;
        if(gather){
            addCommands(new ParallelRaceGroup(getDrive(), new CmdAutoGather(r)));
        } else {
            addCommands(getDrive());
        }
        if(shootDist != 0) addCommands(new AutonShoot(r, shootDist));
    }

    protected AutonDriveAbsolute getDrive(){
        return new MPAutonDriveRelative(r, p, idx);
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
