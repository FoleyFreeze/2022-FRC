package frc.robot.Auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutoSubsytem.AutonInitPos;
import frc.robot.Auton.AutoSubsytem.AutonSimpleShoot;
import frc.robot.Auton.AutoSubsytem.AutonWait;
import frc.robot.Auton.AutoSubsytem.CameraCommands.AutonAbsDrvGthSht;
import frc.robot.Auton.AutoSubsytem.CameraCommands.AutonRelDrvGthSht;
import frc.robot.Auton.AutoSubsytem.ManualCommands.ManAutonAbsDrGthSht;
import frc.robot.Auton.AutoSubsytem.ManualCommands.ManAutonRelDrGthSht;
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

    Position[] manualPList;
    boolean[] manualTodoList;

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

        PositionProvider manualP = new PositionProvider(){

            @Override
            public Position getPosition(int index) {
                return manualPList[index];
            }

            @Override
            public boolean todoList(int index) {
                return manualTodoList[index];
            }  
        };

        //commands that are in common
        addCommands(new AutonInitPos(           r, p, 0),//set initial position
                    new CmdCannonEasyReset(     r),      //re-zero the shooter
                    new AutonSimpleShoot(       r, p, 1),//shoot the ball we have
                    new AutonWait(              r, p, 2) //wait specified time for 1-ball to start driving
                    );   
        if(CalsAuton.useCamera){//camera-only commands 
            addCommands(new AutonRelDrvGthSht(  r, p, 3, false, false),//move out of zone for 1-ball
                        new AutonRelDrvGthSht(  r, p, 4),              //gather & shoot ball 2 (and ball 1 if we still have it), in front of our auton zone
                        new AutonRelDrvGthSht(  r, p, 5),              //gather & shoot ball 3 in front of allied auton zone
                        new AutonAbsDrvGthSht(  r, p, 6, true, false), //move to loading station, gather ball 5
                        new AutonAbsDrvGthSht(  r, p, 7, false, true)  //move back towards goal, shoot
                        );
        } else {//no-camera-only commands
            addCommands(new ManAutonRelDrGthSht(r, manualP, 1, false, false),//move out of zone for 1-ball
                        new ManAutonRelDrGthSht(r, manualP, 2),              //gather & shoot ball 2 (and ball 1 if we still have it), in front of our auton zone
                        new ManAutonRelDrGthSht(r, manualP, 3),              //gather & shoot ball 3 in front of allied auton zone
                        new ManAutonRelDrGthSht(r, manualP, 4, true, false), //move to loading station, gather ball 5
                        new ManAutonAbsDrGthSht(r, manualP, 5, false, true)  //move back towards goal, shoot
                        );
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
