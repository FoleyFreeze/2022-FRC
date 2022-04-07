package frc.robot.Auton.AutoSubsytem.MotionProfiling;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton;
import frc.robot.Auton.AutoSubsytem.AutonDriveAbsolute;
import frc.robot.Auton.AutonSequential.PositionProvider;
import frc.robot.Util.Vector;

public class MPAutonDriveAbsolute extends AutonDriveAbsolute {

    Vector initialVec;

    public MPAutonDriveAbsolute(RobotContainer r, PositionProvider p, int idx){
        super(r, p, idx);
    }

    @Override
    public void initialize(){
        super.initialize();
        initialVec = Vector.subVectors(driveVec, r.sensors.botLoc);
        prevR = initialVec.r;
    }

    protected double prevR;

    @Override
    public void execute(){
        Vector xy = Vector.subVectors(driveVec, r.sensors.botLoc);
        double distDriven = initialVec.r - xy.r;
        double distRemaining = xy.r;
        double vel = (prevR - xy.r) / r.sensors.dt;
        prevR = xy.r;

        boolean accel = xy.r > initialVec.r / 2;
        if(accel){
            xy.r = Math.min(CalsAuton.accelPwr.get() + (CalsAuton.accelSlope.get() * distDriven), CalsAuton.maxPwr.get());
        } else {
            xy.r = Math.min((distRemaining * CalsAuton.decelSlope.get()), CalsAuton.maxPwr.get()) - CalsAuton.decelPwr.get();
        }
         
        if(!r.inputs.getFieldOrient()){
            //if we are not field oriented, act field oriented
            xy.theta -= Math.toRadians(r.sensors.botAng);
        }

        SmartDashboard.putNumber("Auton Rem", distRemaining);
        SmartDashboard.putNumber("Auton Vel", vel);
        SmartDashboard.putNumber("Auton Pwr", xy.r);

        r.drive.driveSwerveAng(xy, rot, CalsAuton.maxSwervePower, CalsAuton.autoSwerveKP, 0);
    }
}
