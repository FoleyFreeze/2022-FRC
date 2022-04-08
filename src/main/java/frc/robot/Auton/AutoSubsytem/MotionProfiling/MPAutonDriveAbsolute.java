package frc.robot.Auton.AutoSubsytem.MotionProfiling;

import edu.wpi.first.wpilibj.Timer;
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

    double startDecelTime;
    double startConstVelTime;
    double startDecelDist;
    double startConstVelDist;

    @Override
    public void initialize(){
        super.initialize();
        initialVec = Vector.subVectors(driveVec, r.sensors.botLoc);

        //dX = (Vf^2 - Vi^2) / 2a
        double vf = CalsAuton.mp_MaxVel.get();
        double vi = 0;
        double a = CalsAuton.mp_MaxAccel.get();
        double distAtMaxVel = (vf*vf - vi*vi) / 2 / a;

        if(distAtMaxVel > initialVec.r / 2){
            //we do not have a constant velocity phase, go right from accel to decel
            startDecelDist = initialVec.r / 2;
            startConstVelDist = startDecelDist;
        } else {
            //we have a constant velocity phase at max vel
            startConstVelDist = distAtMaxVel;
            startDecelDist = initialVec.r - startConstVelDist;
        }

        // dx = vi*t + 0.5*a*t^2
        // vi = 0
        // t = sqrt(2x / a)
        startConstVelTime = Math.sqrt(2 * startConstVelDist / a);
        startDecelTime = startConstVelTime + (startDecelDist - startConstVelDist) / vf;
    }

    @Override
    public void execute(){
        double elapsedTime = Timer.getFPGATimestamp() - starttime;

        Vector xy = Vector.subVectors(driveVec, r.sensors.botLoc);
        
        double a;
        double v;
        double x;
        if(elapsedTime < startConstVelTime) {
            a = CalsAuton.mp_MaxAccel.get();
            v = elapsedTime * a;
            x = 0.5*a*elapsedTime*elapsedTime;
        } else if(elapsedTime < startDecelTime) {
            a = 0;
            v = CalsAuton.mp_MaxVel.get();
            x = startConstVelDist + (elapsedTime - startConstVelTime) * v;
        } else {
            double midV = CalsAuton.mp_MaxAccel.get() * startConstVelTime;
            double t = elapsedTime - startDecelTime;
            a = -CalsAuton.mp_MaxAccel.get();
            v = a * (elapsedTime - (startDecelTime + startConstVelTime));
            x = startDecelDist + midV * t + 0.5 * a * t * t;
        }

        //traveled + remaining = total
        //traveled = total - remaining;
        double error = (initialVec.r - xy.r) - x;
        xy.r = CalsAuton.mp_kS.get() + a * CalsAuton.mp_kA.get() + v * CalsAuton.mp_kV.get() + error * CalsAuton.mp_kP.get(); 
        if(Math.abs(xy.r) > 1){
            SmartDashboard.putNumber("isTooPowerful", elapsedTime);
        }
         
        if(!r.inputs.getFieldOrient()){
            //if we are not field oriented, act field oriented
            xy.theta -= Math.toRadians(r.sensors.botAng);
        }

        r.drive.driveSwerveAng(xy, rot, CalsAuton.maxSwervePower, CalsAuton.autoSwerveKP, 0);
    }
}
