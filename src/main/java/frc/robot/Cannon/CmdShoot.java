package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Vector;

public class CmdShoot extends SequentialCommandGroup{

    RobotContainer r;

    public CmdShoot(RobotContainer r){
        this.r = r;

        addCommands(new SequentialCommandGroup(new CmdPrime(r), new CmdFire(r)));
    }

    @Override
    public void execute(){
        super.execute();

        double x = r.inputs.getDriveX();
        double y = r.inputs.getDriveY();
        double zR;

        Vector xy = Vector.fromXY(x, y);

        if(r.inputs.cameraDrive() && r.sensors.targetAquired()){
            /*double error = r.sensors.botAng - r.sensors.botLoc.theta;
            if(){
                
            }*/
            zR = 0;
        } else {
            zR = r.inputs.getDrivezR();
            Inputs.mapSquareToCircle(xy);
        }

        r.drive.driveSwerve(xy, zR);
    } 

}
