package frc.robot.Auton.AutoSubsytem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Auton.CalsAuton.Position;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;

public class AutonDriveAbsolute extends CommandBase {
    
    protected RobotContainer r;

    protected Vector driveVec;
    protected double rot;

    protected PositionProvider p;
    protected int idx;

    protected double starttime = 0;

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
        SmartDashboard.putString("AutoDriveRem",xy.toStringXY());

        if(xy.r > CalsAuton.maxDrivePower){
            xy.r = CalsAuton.maxDrivePower;
        }
         
        if(!r.inputs.getFieldOrient()){
            //if we are not field oriented, act field oriented
            xy.theta -= Math.toRadians(r.sensors.botAng);
        }

        r.drive.driveSwerveAng(xy, rot, CalsAuton.maxSwervePower, CalsAuton.autoSwerveKP, 0);
    }

    @Override
    public void end(boolean interrupted){
        r.drive.driveSwerve(new Vector(0,0), 0);
    }

    double prevDelta;
    @Override
    public boolean isFinished(){
        if(driveVec == null) return true;

        Vector toTarget = Vector.subVectors(driveVec, r.sensors.botLoc);
        double dist = Math.abs(toTarget.r);
        boolean driveDone = prevDelta <= dist && Timer.getFPGATimestamp() > starttime + 0.5;
        //System.out.println("DeltaDelta " + (dist - prevDelta));
        prevDelta = dist;

        double deltaAng = Angle.normDeg(rot - r.sensors.botAng);
        boolean angleDone = Math.abs(deltaAng) < CalsAuton.minAutoAngError;

        if(driveDone && !angleDone){
            System.out.println("Drive beat the angle: " + Math.abs(deltaAng));
        }
        return driveDone && angleDone || Timer.getFPGATimestamp() > starttime + CalsAuton.autonDriveTime;
    }
}
