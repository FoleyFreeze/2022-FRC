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
        System.out.println("Started AutoGather");
    }

    double startTime;
    boolean prevLoadState = false;
    double lowFallingTimer;
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
                r.intake.intake(0);
            } else {
                r.intake.intake(0);
            }
        }

        
        if(r.sensors.ballSensorLower.fallingEdge()){
            lowFallingTimer = Timer.getFPGATimestamp();
        }

        //transporter motor
        if(lowBall){
            if(hiBall){
                r.cannon.stopTransport();
            } else {
                r.cannon.transport();
            }
        } else {
            if(!hiBall && Timer.getFPGATimestamp() < lowFallingTimer + r.intake.cals.lowFallingTime){
                r.cannon.transport();
            } else if(hiBall) {
                r.cannon.transport();
            } else {
                //run when we have nothing
                r.cannon.transport();
            }
        }

        //kicker motor
        if(!hiBall && Timer.getFPGATimestamp() < lowFallingTimer + r.intake.cals.lowFallingKickerOffset){
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
        r.cannon.transport(0);
        r.intake.intake(0);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
