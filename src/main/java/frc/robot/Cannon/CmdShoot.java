package frc.robot.Cannon;

import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

public class CmdShoot extends SequentialCommandGroup{
    
    RobotContainer r;

    public CmdShoot(RobotContainer r){
        this.r = r;
        addCommands(new FunctionalCommand(this::doNothing, r.cannon::prime, this::doNothing, r.cannon::upToSpeed, r.cannon));
        //addCommands(new ParallelDeadlineGroup(new WaitCommand(r.cannon.cals.fireTime), new InstantCommand(r.cannon::prime, r.cannon), new InstantCommand(r.cannon::fire), r.cannon));
        //addCommands(new ParallelCommandGroup(new InstantCommand(r.cannon.stopPrime), new InstantCommand(r.cannon::stopFire)));
    }

    public void doNothing(){
    }

    public void doNothing(boolean b){
    }
}
