package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutonSequential.PositionProvider;

public class AutonWait extends CommandBase{

    RobotContainer r;
    PositionProvider p;
    int idx;

    double startTime;
    
    public AutonWait(RobotContainer r, PositionProvider p, int idx){
        this.r = r;
        this.p = p;
        this.idx = idx;
    }

    @Override
    public void initialize(){
        if(p.todoList(idx)) startTime = Timer.getFPGATimestamp();
        System.out.println("Auton " + idx);
    }

    @Override
    public boolean isFinished(){
        return startTime + r.waitTime.getSelected() < Timer.getFPGATimestamp() || !p.todoList(idx);
    }
}
