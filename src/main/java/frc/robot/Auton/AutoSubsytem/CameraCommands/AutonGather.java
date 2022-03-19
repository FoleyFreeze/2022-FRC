package frc.robot.Auton.AutoSubsytem.CameraCommands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.RobotContainer;
import frc.robot.Intake.CmdAutoGather;

public class AutonGather extends ParallelRaceGroup {

    RobotContainer r;
    boolean topBall;

    public AutonGather(RobotContainer r){
        this.r = r;
        addCommands(new CmdAutoGather(r));
    }

    @Override
    public void initialize(){
        topBall = r.sensors.ballSensorUpper.get();
    }

    @Override
    public boolean isFinished(){
        super.isFinished();
        if(topBall){
            return r.sensors.ballSensorLower.get();
        } else {
            return r.sensors.ballSensorUpper.get();
        }
    }
}
