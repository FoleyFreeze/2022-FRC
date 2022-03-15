package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutonSequential.PositionProvider;

public class AutonInitPos extends CommandBase{

    RobotContainer r;
    PositionProvider p;
    int idx;

    public AutonInitPos(RobotContainer r, PositionProvider p, int idx){
        this.r = r;
        this.p = p;
        this.idx = idx;
    }

    @Override
    public void initialize(){
        //r.sensors.botLoc = p.getPosition(idx).v;
        //r.sensors.botAng = p.getPosition(idx).a;
        r.sensors.navX.overrideAng(p.getPosition(idx).a);
        r.sensors.encoders.resetPos(p.getPosition(idx).v);
        System.out.println("Auton " + idx);
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
