package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Util.Vector;

public class CmdAutoDrive extends CommandBase {
    
    RobotContainer r;

    Vector driveVec;
    double rot;

    public CmdAutoDrive(RobotContainer r, Vector driveVec, double rot){
        this.r = r;
        addRequirements(r.drive);

        this.driveVec = driveVec;
        this.rot = rot;
    }

    @Override
    public void initialize(){

    }

    @Override
    public boolean runsWhenDisabled(){
        return true;
    }

    @Override
    public void execute(){
        r.drive.driveSwerve(driveVec, rot);
    }

    @Override
    public void end(boolean interrupted){
        r.drive.driveSwerve(new Vector(0,0), 0);
    }

    @Override
    public boolean isFinished(){
        double xError = Math.abs(r.sensors.botLoc.getX() - driveVec.getX());
        double yError = Math.abs(r.sensors.botLoc.getY() - driveVec.getY());
        double zError = Math.abs(r.sensors.botAng - rot);

        return false;
    }
}
