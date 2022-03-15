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
        System.out.println("Auton " + idx);
    }

    @Override
    public void execute(){
        if(p.todoList(idx)){
            r.cannon.prime(CalsAuton.simpleShootPrimeSpeed, CalsAuton.simpleShootPrimeAng);
            if(Timer.getFPGATimestamp() > CalsAuton.primeTime + startTime){
                r.cannon.fire(CalsAuton.simpleShootFirePwr);
            }
        }
    }

    @Override
    public boolean isFinished(){
        return Timer.getFPGATimestamp() > startTime + CalsAuton.shootTime || !p.todoList(idx);
    }

    @Override
    public void end(boolean interrupted){
        r.cannon.setSpeed(0, 0);
        r.cannon.fire(0);
    }
}
