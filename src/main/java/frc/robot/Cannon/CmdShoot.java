package frc.robot.Cannon;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Intake.CmdReload;
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
        prevAngles = new double[r.cannon.cals.maxTgtImgs.getAsInt()];
    }

    double filtAngle;
    double prevAngle;
    double kU = 0.1;
    boolean first;
    int imgCt;
    double[] prevAngles;

    @Override
    public void execute(){
        super.execute();

        double x = r.inputs.getDriveX();
        double y = r.inputs.getDriveY();
        double zR;

        Vector xy = Vector.fromXY(x, y);
        
        Inputs.mapSquareToCircle(xy);

        if(r.inputs.cameraDrive() && r.sensors.hasTargetImage() && !r.inputs.driverJoy.layUpShot() && r.inputs.operatorJoy.hubSwitch()){
            Vector targetPos = Vector.subVectors(r.sensors.target.location, r.sensors.botLoc);
            SmartDashboard.putString("BotRelTgt", targetPos.toStringXY());

            double tgtAngle = Math.toDegrees(targetPos.theta);
            
            if(imgCt < prevAngles.length){
                prevAngles[imgCt] = tgtAngle;
                filtAngle = r.sensors.botAng;
                imgCt++;
            } else {
                int idx = imgCt % prevAngles.length;
                prevAngles[idx] = tgtAngle;
                imgCt++;
                double minAng = 361;
                double maxAng = -361;
                double sum = 0;
                for(double d : prevAngles){
                    sum += d;
                    if(d > maxAng) maxAng = d;
                    if(d < minAng) minAng = d;
                }
                sum -= maxAng;
                sum -= minAng;
                sum /= prevAngles.length - 2;
                filtAngle = sum;
            }
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
            

            r.drive.driveSwerveAng(xy, filtAngle, r.cannon.cals.maxPower, r.cannon.cals.drivekR.get(), r.cannon.cals.drivekD.get());
        } else {
            zR = r.inputs.getDrivezR();
            if(zR > r.cannon.cals.maxPower) zR = r.cannon.cals.maxPower;
            r.drive.driveSwerve(xy, zR);
        }
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
