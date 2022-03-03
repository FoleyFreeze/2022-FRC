package frc.robot.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Vector;

public class CmdGather extends CommandBase{
    
    RobotContainer r;

    public CmdGather(RobotContainer r){
        this.r = r;
        addRequirements(r.intake);
        addRequirements(r.drive);
        //addRequirements(r.cannon);
    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){
        double x;
        double y;
        double zR;

        Vector xy;
        
        r.intake.intake();
        r.cannon.transport();

        if(r.inputs.cameraDrive() && r.sensors.hasAlliedCargo()){
            Vector cargoPos = Vector.subVectors(r.sensors.alliedCargo.location, r.sensors.botLoc);
            cargoPos.theta -= Math.toRadians(r.sensors.botAng);
            
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
    public void end(boolean interrupted){
        if(interrupted){
            r.cannon.stopTransport();
        }
        
        r.intake.stop();
        r.cannon.preLoadCargo();
    }

    @Override
    public boolean isFinished(){
        return r.sensors.hasGatheredCargo();
    }
}

