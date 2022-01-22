package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Util.Vector;

public class CmdDrive extends CommandBase {
    
    RobotContainer bot;

    public CmdDrive(RobotContainer r){
        bot = r;
        addRequirements(r.drive);
    }

    @Override
    public void initialize(){
        bot.drive.resetWheelEncoders();
    }

    @Override
    public boolean runsWhenDisabled(){
        return true;
    }

    @Override
    public void execute(){
        double x = bot.inputs.getDriveX();
        double y = bot.inputs.getDriveY();
        double zR = bot.inputs.getDrivezR();

        Vector xy = Vector.fromXY(x, y);
        mapSquareToCircle(xy);
        
        bot.drive.driveSwerve(xy, zR);
    }

    public static void mapSquareToCircle(Vector v){
        double pi = Math.PI;
        double maxMag;

        if(v.theta < 3*pi/4 && v.theta > pi/4
                || v.theta < -pi/4 && v.theta > -3*pi/4){
            maxMag = Math.abs(1 / Math.sin(v.theta));
        } else {
            maxMag = Math.abs(1 / Math.cos(v.theta));
        }

        v.r /= maxMag;
    }

    @Override
    public void end(boolean interrupted){

    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
