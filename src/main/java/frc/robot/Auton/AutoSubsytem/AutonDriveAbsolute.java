package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutonSequential;
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
        if(driveVec == null) return true;

        Vector deltaPos = Vector.subVectors(driveVec, r.sensors.botLoc);
        boolean driveDone = Math.abs(deltaPos.r) < r.drive.cals.minAutoPosError;

        double deltaAng = Angle.normDeg(rot - r.sensors.botAng);
        boolean angleDone = deltaAng < r.drive.cals.minAutoAngError;

        return driveDone && angleDone;
    }
}
