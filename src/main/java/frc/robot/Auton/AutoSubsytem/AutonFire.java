package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;

public class AutonFire extends CommandBase{

    RobotContainer r;

    public AutonFire(RobotContainer r){
        this.r = r;
        addRequirements(r.cannon);
    }

    @Override
    public void execute(){
        r.cannon.prime();
        if(r.sensors.target.location.r < CalsAuton.minShootDist && r.sensors.hasTargetImage()){
            r.cannon.fire();
        }
    }

    @Override
    public void end(boolean interrupted){
        r.cannon.setSpeed(0, 0);
        r.cannon.fire(0);
    }
}
