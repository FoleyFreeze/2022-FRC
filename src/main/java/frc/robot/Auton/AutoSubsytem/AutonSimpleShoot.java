package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
import frc.robot.Auton.AutonSequential.PositionProvider;

public class AutonSimpleShoot extends CommandBase{

    RobotContainer r;
    PositionProvider p;
    int idx;
    double startTime;
    double shootStart;
    boolean shootStartSet = false;

    boolean twoBalls;

    public AutonSimpleShoot(RobotContainer r, PositionProvider p, int idx){
        this.r = r;
        this.p = p;
        this.idx = idx;
    }

    @Override
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
        twoBalls = r.sensors.ballSensorLower.get();
    }

    @Override
    public void execute(){
        if(p.todoList(idx)){
            r.cannon.prime(CalsAuton.simpleShootPrimeSpeed, CalsAuton.simpleShootPrimeAng);
            if(startTime + Timer.getFPGATimestamp() > CalsAuton.primeTime){
                r.cannon.fire(CalsAuton.simpleShootFirePwr);
            }
        }
    }

    @Override
    public boolean isFinished(){
        return startTime + Timer.getFPGATimestamp() > CalsAuton.shootTime || !p.todoList(idx);
    }
}
