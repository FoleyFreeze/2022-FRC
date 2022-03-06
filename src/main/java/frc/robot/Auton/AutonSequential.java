package frc.robot.Auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.CalsAuton.Position;
import frc.robot.Cannon.CmdShoot;

public class AutonSequential extends SequentialCommandGroup{
    
    RobotContainer r;
    CalsAuton cals;

    public interface PositionProvider {
        public Position getPosition(int index);
        public boolean getSkip(int index);
    }

    Position[] positionList;
    boolean[] skipList;

    public AutonSequential(RobotContainer r, CalsAuton cals){
        this.r = r;
        this.cals = cals;

        PositionProvider p = new PositionProvider(){
            public Position getPosition(int index){
                return positionList[index];
            }
            public boolean getSkip(int index){
                return skipList[index];
            }
        };

        addCommands(new AutonRelDriveGatherShoot(r, p, 0, false), //shoot the ball we have
                    new AutonRelDriveGatherShoot(r, p, 1), //shoot ball 2 (and ball 1 if we still have it), in front of our auton zone
                    new SequentialCommandGroup(new AutoDriveRelative(r, p, 1), new CmdShoot(r)), //shoot the ball in front of our partners zone
                    new SequentialCommandGroup(new AutoDriveAbsolute(r, p, 2), new CmdShoot(r))); //shoot the ball in front of the load station
    }

    @Override
    public void initialize() {
        super.initialize();

        positionList = cals.positionList[r.posChooser.getSelected()];
        skipList = cals.skipLists[r.ballCtChooser.getSelected()];

        switch(r.ballCtChooser.getSelected()){
            case 2: //3ball close
            case 4: //4ball close
            case 6: //5ball
                //if in an incompatable position then force single ball auton
                if(r.posChooser.getSelected() != 2) skipList = cals.skipLists[0]; 
            break;
        }
    }

}
