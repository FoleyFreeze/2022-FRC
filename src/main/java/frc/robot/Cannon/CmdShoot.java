package frc.robot.Cannon;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Intake.CmdReload;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;

public class CmdShoot extends SequentialCommandGroup{

    RobotContainer r;

    CmdFire fireCmd;

    public CmdShoot(RobotContainer r){
        addRequirements(r.cannon);
        addRequirements(r.drive);
        this.r = r;

        fireCmd = new CmdFire(r);
        addCommands(new SequentialCommandGroup(new CmdPrime(r), 
                                               fireCmd,
                                               new CmdReload(r),
                                               new CmdReFire(r)));
    }

    @Override
    public void initialize(){
        System.out.println("Cmd Shoot Init");
        super.initialize();
        r.sensors.enableTgtLights(true);
        first = true;
        imgCt = 0;
        prevXs = new double[r.cannon.cals.maxTgtImgs.getAsInt()];
        prevYs = new double[r.cannon.cals.maxTgtImgs.getAsInt()];
        fireCmd.endEarly = false;
    }

    boolean first;
    int imgCt;
    double[] prevXs;
    double[] prevYs;

    @Override
    public void execute(){
        super.execute();

        double x = r.inputs.getDriveX();
        double y = r.inputs.getDriveY();
        double zR;

        Vector xy = Vector.fromXY(x, y);
        
        Inputs.mapSquareToCircle(xy);

        if(r.inputs.cameraDrive() && r.sensors.hasTargetImage() && !r.inputs.operatorJoy.layUpShot() && r.inputs.operatorJoy.hubSwitch()){
            Vector targetPos = Vector.subVectors(r.sensors.target.location, r.sensors.botLoc);
            
            //correct for shooter location
            targetPos.theta += Math.PI/2;
            double tgtAngle = Math.toDegrees(targetPos.theta);
            
            double tgtX = updateFilter(imgCt, prevXs, targetPos.getX());
            double tgtY = updateFilter(imgCt, prevYs, targetPos.getY());
            imgCt++;
            
            targetPos.theta = Angle.normRad(targetPos.theta - Math.toRadians(r.sensors.botAng));
            SmartDashboard.putString("BotRelTgt", targetPos.toStringPolar());

            /*
            if(first) {
                first = false;
                filtAngle = tgtAngle;
            } else if(tgtAngle != prevAngle && imgCt < r.cannon.cals.maxTgtImgs.get()) {
                imgCt++;
                filtAngle = filtAngle * (1-kU) + tgtAngle * kU;
            }
            prevAngle = tgtAngle;
            */

            Vector filtLoc = Vector.fromXY(tgtX,tgtY);

            double kR;
            double angDiff = Math.abs(Angle.normDeg(Math.toDegrees(filtLoc.theta) - r.sensors.botAng));
            if(angDiff < 45){
                double blendRatio = (45 - angDiff) / 45;
                kR = r.cannon.cals.drivekR45.get() * (1-blendRatio) + r.cannon.cals.drivekR0.get() * blendRatio;
            } else {
                kR = r.cannon.cals.drivekR45.get();
            }

            //determine angle feed forward
            /*
            Vector xyDist = new Vector(xy);
            xyDist.r *= r.drive.cals.FFangleDist.get();
            if(!r.inputs.getFieldOrient()){
                xyDist.theta = Angle.normRad(xyDist.theta + Math.toRadians(r.sensors.botAng));
            }
            double origAngle = Math.toDegrees(targetPos.theta);
            xyDist.add(targetPos);
            double finalAngle = Math.toDegrees(xyDist.theta);
            double angleDelta = Angle.normDeg(finalAngle - origAngle);
            double ffPwr = angleDelta * r.drive.cals.FFanglePwrPerDeg.get();
            */
            //turn into robot relative (facing backwards)
            targetPos.theta = Angle.normRad(targetPos.theta - Math.PI/2);

            r.drive.driveSwerveAng(xy, Math.toDegrees(filtLoc.theta), r.cannon.cals.maxPower, kR, r.cannon.cals.drivekD.get(), targetPos);
        } else {
            zR = r.inputs.getDrivezR();
            if(zR > r.cannon.cals.maxPower) zR = r.cannon.cals.maxPower;
            r.drive.driveSwerve(xy, zR);
        }
    }

    private double updateFilter(int imgCt, double[] array, double newVal){
        double retVal;
        if(imgCt < array.length){
            array[imgCt] = newVal;
            retVal = newVal;
        } else {
            int idx = imgCt % array.length;
            array[idx] = newVal;
            double minAng = 361;
            double maxAng = -361;
            double sum = 0;
            for(double d : array){
                sum += d;
                if(d > maxAng) maxAng = d;
                if(d < minAng) minAng = d;
            }
            sum -= maxAng;
            sum -= minAng;
            sum /= array.length - 2;
            retVal = sum;
        }

        return retVal;
    }

    @Override
    public void end(boolean interrupted){
        super.end(interrupted);
        r.sensors.enableTgtLights(false);
        System.out.println("Cmd Shoot End");
        r.cannon.setPower(0, 0); 
        r.cannon.fire(0);
    }

    @Override
    public boolean isFinished(){
        return super.isFinished() || fireCmd.endEarly;
    }
}
