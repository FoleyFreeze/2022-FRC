package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Vector;

public class CmdDrive extends CommandBase{
    RobotContainer bot;

    public CmdDrive(RobotContainer r){
        bot = r;
        addRequirements(r.drive);
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
        double x = bot.inputs.getDriveX();
        double y = bot.inputs.getDriveY();
        double zR = bot.inputs.getDrivezR();

        Vector xy = Vector.fromXY(x, y);
        Inputs.mapSquareToCircle(xy);
        
        bot.drive.driveSwerve(xy, zR);
    }

    

    @Override
    public void end(boolean interrupted){

    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
