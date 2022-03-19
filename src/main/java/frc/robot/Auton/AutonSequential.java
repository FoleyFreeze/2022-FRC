package frc.robot.Auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutoSubsytem.AutonInitPos;
import frc.robot.Auton.AutoSubsytem.AutonSimpleShoot;
import frc.robot.Auton.AutoSubsytem.AutonWait;
import frc.robot.Auton.AutoSubsytem.CameraCommands.AutonAbsDrvGthrShoot;
import frc.robot.Auton.AutoSubsytem.CameraCommands.AutonRelDrvGthrShoot;
import frc.robot.Auton.CalsAuton.Position;
import frc.robot.Cannon.CmdCannonEasyReset;

public class AutonSequential extends SequentialCommandGroup{
    
    RobotContainer r;
    CalsAuton cals;

    public interface PositionProvider {
        public Position getPosition(int index);
        public boolean todoList(int index);
    }

    Position[] positionList;
    boolean[] todoList;
    double waitTime;

    public AutonSequential(RobotContainer r, CalsAuton cals){
        this.r = r;
        this.cals = cals;

        PositionProvider p = new PositionProvider(){
            public Position getPosition(int index){
                return positionList[index];
            }
            public boolean todoList(int index){
                return todoList[index];
            }
        };

        //commands that are in common
        addCommands(new AutonInitPos(            r, p, 0),              //set initial position
                    new CmdCannonEasyReset(      r),                    //re-zero the shooter
                    new AutonSimpleShoot(        r, p, 1),              //shoot the ball we have
                    new AutonWait(               r, p, 2)               //wait specified time for 1-ball to start driving
                    );
        if(CalsAuton.useCamera){//camera-only commands
            addCommands(new AutonRelDrvGthrShoot(r, p, 3, false, false),//move out of zone for 1-ball
                        new AutonRelDrvGthrShoot(r, p, 4),              //gather & shoot ball 2 (and ball 1 if we still have it), in front of our auton zone
                        new AutonRelDrvGthrShoot(r, p, 5),              //gather & shoot ball 3 in front of allied auton zone
                        new AutonAbsDrvGthrShoot(r, p, 6, true, false), //move to loading station, gather ball 5
                        new AutonAbsDrvGthrShoot(r, p, 7, false, true)  //move back towards goal, shoot
                        );
        } else {//no-camera-only commands
            
        }

        addRequirements(r.cannon, r.intake, r.drive);
    }

    @Override
    public void initialize() {
        
        positionList = cals.positionList[r.posChooser.getSelected()];
        todoList = cals.todoLists[r.ballCtChooser.getSelected()];
        waitTime = r.waitTime.getSelected();

        /*
        switch(r.ballCtChooser.getSelected()){
            case 2: //3ball close
            case 4: //4ball close
            case 6: //5ball
                //if in an incompatable position then force single ball auton
                if(r.posChooser.getSelected() != 2) todoList = cals.todoLists[0]; 
            break;
        }
        */
        //TODO: make this work!

        super.initialize();
    }

}
