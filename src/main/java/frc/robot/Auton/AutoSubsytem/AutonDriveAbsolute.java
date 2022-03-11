package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Auton.CalsAuton.Position;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;

public class AutonDriveAbsolute extends CommandBase {
    
    RobotContainer r;

    Vector driveVec;
    double rot;

    PositionProvider p;
    int idx;

    double timer = 2;
    double starttime = 0;

    public AutonDriveAbsolute(RobotContainer r, PositionProvider p, int idx){
        this.r = r;
        this.p = p;
        this.idx = idx;
        addRequirements(r.drive);
    }

    @Override
    public void initialize(){
        Position pos = p.getPosition(idx);
        if(pos != null){
            driveVec = pos.v;
            rot = pos.a;
        }
        starttime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute(){
        Vector xy = Vector.subVectors(driveVec, r.sensors.botLoc);
        if(xy.r > CalsAuton.maxDrivePower){
            xy.r = CalsAuton.maxDrivePower;
        }
        double angle = CalsAuton.autoSwerveKP * Angle.normDeg(rot - r.sensors.botAng);
        if(angle > CalsAuton.maxSwervePower){
            angle = CalsAuton.maxSwervePower;
        }

        if(r.inputs.getFieldOrient()){
            //if we are field oriented, offset so that we stay robot oriented
            xy.theta += Math.toRadians(r.sensors.botAng);
        }
        r.drive.driveSwerve(xy, angle);
    }

    @Override
    public void end(boolean interrupted){
        r.drive.driveSwerve(new Vector(0,0), 0);
    }

    @Override
    public boolean isFinished(){
        if(driveVec == null) return true;

        Vector deltaPos = Vector.subVectors(driveVec, r.sensors.botLoc);
        boolean driveDone = Math.abs(deltaPos.r) < CalsAuton.minAutoPosError;

        double deltaAng = Angle.normDeg(rot - r.sensors.botAng);
        boolean angleDone = deltaAng < CalsAuton.minAutoAngError;

        return driveDone && angleDone || Timer.getFPGATimestamp() > starttime + timer;
    }
}
