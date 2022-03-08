package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;

public class AutonSimpleShoot extends CommandBase{

    RobotContainer r;
    double startTime;
    double shootStart;
    boolean shootStartSet = false;

    boolean twoBalls;

    public AutonSimpleShoot(RobotContainer r){
        this.r = r;
    }

    @Override
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
        twoBalls = r.sensors.ballSensorLower.get();
    }

    @Override
    public void execute(){
        r.cannon.prime(CalsAuton.simpleShootPrimePwr, CalsAuton.simpleShootPrimeAng);
        if(startTime + Timer.getFPGATimestamp() > CalsAuton.primeTime){
            r.cannon.fire(CalsAuton.simpleShootFirePwr);
        }
    }

    @Override
    public boolean isFinished(){
        return startTime + Timer.getFPGATimestamp() > CalsAuton.shootTime;
    }
}
