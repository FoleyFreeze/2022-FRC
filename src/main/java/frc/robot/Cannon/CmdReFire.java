package frc.robot.Cannon;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;

public class CmdReFire extends CmdFire{

    RobotContainer r;
    
    double angleDiff;

    boolean ran;

    public CmdReFire(RobotContainer r){
        super(r);
        this.r = r;
        addRequirements(r.cannon);
    }

    @Override
    public void initialize(){
        ran = false;
    }

    @Override
    public void execute(){
        r.cannon.prime();
        if(r.cannon.angleAligned()) {
            ran = true;
            r.cannon.fire();
        }

        if(!ran) startTime = Timer.getFPGATimestamp();
    }

    @Override
    public boolean isFinished(){
        return Timer.getFPGATimestamp() > startTime + r.cannon.cals.shootTimeOne;
    }
    
}