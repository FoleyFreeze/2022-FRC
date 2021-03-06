package frc.robot.Intake;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Vector;

public class CmdAutoGather extends CommandBase{

    RobotContainer r;

    boolean reloadMode;

    public CmdAutoGather(RobotContainer r, boolean reloadMode){
        this.r = r;
        this.reloadMode = reloadMode;
        if(!reloadMode && !DriverStation.isAutonomous()){
            addRequirements(r.drive);
        }
        addRequirements(r.intake);
        //no cannon requirement even though we use it
    }

    public CmdAutoGather(RobotContainer r){
        this(r, false);
    }

    @Override
    public void initialize(){
        System.out.println("AutoGather init");
        if(!reloadMode) r.sensors.enableCargoLights(true);
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
        if(!reloadMode){
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
                    r.intake.intake();
                }
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
            r.cannon.setAngle(r.cannon.cals.resetAngle);
        }

        //drive code
        if(!reloadMode && (!DriverStation.isAutonomous() || CalsAuton.useCamera)){
            double x;
            double y;
            double zR;
            Vector xy;

            double startTime = 0;
            boolean startTimeSet = false;

            if(r.inputs.cameraDrive() && allowDrive && r.sensors.hasAlliedCargo()){
                
                Vector cargoPos = Vector.subVectors(r.sensors.alliedCargo.location, r.sensors.botLoc);
                cargoPos.theta -= Math.toRadians(r.sensors.botAng);
                
                SmartDashboard.putString("BotRelCargo", cargoPos.toStringXY());
                
                if(cargoPos.r > r.intake.cals.maxAnglePIDDist.get()){
                    zR = r.intake.cals.kR.get() * (cargoPos.theta - Math.PI/2);//correct for gatherer location
                } else {
                    zR = 0;
                }

                //logic for cargo going too close & out of camera view
                if(cargoPos.getY() > r.intake.cals.minCargoDist.get() && Math.abs(cargoPos.getX()) > r.intake.cals.minCargoXError && Timer.getFPGATimestamp() > startTime + r.intake.cals.extraGatherTime.get()){
                    x = r.intake.cals.kX.get() * cargoPos.getX();
                    y = Math.max(r.intake.cals.yPower.get() - Math.abs(x) - Math.abs(zR), 0);
                    y += r.intake.cals.kY.get() * (cargoPos.getY() - r.intake.cals.gatherYOffset.get());
                    startTimeSet = false;
                } else {//once the ball is within a certain window of distance, set the power directly
                    if(!startTimeSet){
                        startTimeSet = true;
                        startTime = Timer.getFPGATimestamp();
                    }
                    zR = 0;
                    x = 0;
                    y = r.intake.cals.yPower.get();
                }
            
                xy = Vector.fromXY(x, y);
                    
                if(xy.r > r.intake.cals.autoBallMaxPwr.get()){
                    xy.r = r.intake.cals.autoBallMaxPwr.get();
                }
                
                if(r.inputs.getFieldOrient()){
                    //if we are field oriented, offset so that we stay robot oriented
                    xy.theta += Math.toRadians(r.sensors.botAng);
                }

            } else {
                zR = r.inputs.getDrivezR();
                x = r.inputs.getDriveX();
                y = r.inputs.getDriveY();

                xy = Vector.fromXY(x, y);
                Inputs.mapSquareToCircle(xy);
            }
            
            r.drive.driveSwerve(xy, zR);
        }
    }

    @Override
    public void end(boolean isFinished){
        System.out.println("AutoGather end");
        r.cannon.transport(0);
        r.intake.intake(0);
        r.sensors.enableCargoLights(false);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
