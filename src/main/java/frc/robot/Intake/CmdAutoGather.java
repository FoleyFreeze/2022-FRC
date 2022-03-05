package frc.robot.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class CmdAutoGather extends CommandBase{

    RobotContainer r;

    public CmdAutoGather(RobotContainer r){
        this.r = r;
        addRequirements(r.intake);
        //no cannon requirement even though we use it
    }

    @Override
    public void initialize(){
        
    }

    double startTime;
    boolean prevLoadState = false;
    @Override
    public void execute(){
        boolean hiBall = r.sensors.ballSensorUpper.get();
        boolean lowBall = r.sensors.ballSensorLower.get();

        //gatherer motor
        if(!lowBall){
            r.intake.intake();
            startTime = Timer.getFPGATimestamp();
        } else if(lowBall && Timer.getFPGATimestamp() > startTime + r.intake.cals.intakeTimeOffset){
            if(hiBall){
                //set defensive intake position
            } else {
                r.intake.intake(0);
            }
        }

        //transporter motor
        if(lowBall){
            if(hiBall){
                r.cannon.stopTransport();
            } else {
                r.cannon.transport();
            }
        } else {
            r.cannon.stopTransport();
        }

        //kicker motor
        if(!hiBall && lowBall){
            if(!prevLoadState) {
                r.cannon.preLoadCargo();
                prevLoadState = true;
            }
        } else{
            prevLoadState = false;
        }
    }

    @Override
    public void end(boolean isFinished){

    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
