package frc.robot.Intake;

import java.util.Currency;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;

public class CmdAutoGather extends CommandBase{

    RobotContainer r;

    public CmdAutoGather(RobotContainer r){
        this.r = r;
        addRequirements(r.drive);
        addRequirements(r.intake);
        //no cannon requirement even though we use it
    }

    @Override
    public void initialize(){
        //System.out.println("Started AutoGather");
    }

    double intakeDelayStartTime;
    boolean prevLoadState = false;
    double lowFallingTimer;
    double gatherMovementTimer;
    double gatherPrevPosition;
    double gatherCurrentTimer;
    double gatherFlipTimer;
    double gatherFlipSetpoint;
    @Override
    public void execute(){
        boolean hiBall = r.sensors.ballSensorUpper.get();
        boolean lowBall = r.sensors.ballSensorLower.get();

        boolean allowDrive = false;
        double time = Timer.getFPGATimestamp();

        //gatherer motor
        if(!lowBall){
            r.intake.intake();
            allowDrive = true;
            intakeDelayStartTime = time;
        } else if(lowBall && time > intakeDelayStartTime + r.intake.cals.intakeTimeOffset){
            if(hiBall){
                //set defensive intake position
                double currAngle = r.intake.intakeMotor.getPosition() * 360;
                double setpoint;
                double angleDiff = (currAngle % 180) + 30;
                if(angleDiff > 90){
                    setpoint = currAngle + (180 - angleDiff);
                } else {
                    setpoint = currAngle - angleDiff;
                }

                //if a ball is "stuck" with the flaps in the vertical position, remove it
                if(Math.abs(currAngle - gatherPrevPosition) > 10){
                    gatherMovementTimer = time;
                    gatherPrevPosition = currAngle;
                }
                if(r.sensors.pdh.getCurrent(r.intake.cals.intakeMotor.channel) < 10){
                    gatherCurrentTimer = time;
                }

                if(time > gatherFlipTimer + 0.5){
                    if(time > gatherMovementTimer + 0.5 && time > gatherCurrentTimer + 0.5){
                        gatherFlipSetpoint = setpoint - 180;
                        gatherFlipTimer = time;
                        setpoint = gatherFlipSetpoint;
                    }
                } else {
                    setpoint = gatherFlipSetpoint;
                }
                
                r.intake.intakeMotor.setPosition(setpoint / 360.0);
                
            } else {
                //one ball gathered, still in lower slot
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
        if(!hiBall && time < lowFallingTimer + r.intake.cals.lowFallingKickerTimeOffset){
            if(!prevLoadState) {
                r.cannon.preLoadCargo();
                prevLoadState = true;
            }
        } else{
            prevLoadState = false;
        }

        //cannon motor
        if(lowBall && !hiBall){
            r.cannon.setAngle(65);
        }

        //drive code
        double x;
        double y;
        double zR;
        Vector xy;

        if(r.inputs.cameraDrive() && r.sensors.hasAlliedCargo() && allowDrive){
            Vector cargoPos = Vector.subVectors(r.sensors.alliedCargo.location, r.sensors.botLoc);
            cargoPos.theta -= Math.toRadians(r.sensors.botAng);
            
            SmartDashboard.putString("BotRelCargo", cargoPos.toStringXY());

            zR = r.intake.cals.kR * cargoPos.theta;
            x = r.intake.cals.kX * cargoPos.getX();
            y = Math.max(r.intake.cals.yPower - x - zR, 0);
            xy = Vector.fromXY(x, y);
        } else {
            zR = r.inputs.getDrivezR();
            x = r.inputs.getDriveX();
            y = r.inputs.getDriveY();

            xy = Vector.fromXY(x, y);
            Inputs.mapSquareToCircle(xy);
        }

        r.drive.driveSwerve(xy, zR);
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
