package frc.robot.Auton;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Auton.AutoSubsytem.AutonInitPos;
import frc.robot.Auton.AutoSubsytem.AutonSimpleShoot;
import frc.robot.Auton.AutoSubsytem.AutonWait;
import frc.robot.Auton.AutoSubsytem.CameraCommands.AutonAbsDrvGthSht;
import frc.robot.Auton.AutoSubsytem.CameraCommands.AutonRelDrvGthSht;
import frc.robot.Auton.AutoSubsytem.ManualCommands.ManAutonAbsDrGthSht;
import frc.robot.Auton.AutoSubsytem.ManualCommands.ManAutonGatherOnly;
import frc.robot.Auton.AutoSubsytem.ManualCommands.ManAutonRelDrGthSht;
import frc.robot.Auton.CalsAuton.Position;
import frc.robot.Cannon.CmdCannonEasyReset;

public class AutonSequential extends SequentialCommandGroup{
    
    double autonStartTime;

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

        /*
        PositionProvider manualP = new PositionProvider(){
            public Position getPosition(int index) {
                return manualPList[index];
            }
            public boolean todoList(int index) {
                return manualTodoList[index];
            }  
        };
        */

        //commands that are in common
        addCommands(new AutonInitPos(           r, p, 0),//set initial position
                    new CmdCannonEasyReset(     r),      //re-zero the shooter
                    new AutonSimpleShoot(       r, p, 1),//shoot the ball we have
                    new AutonWait(              r, p, 2) //wait specified time for 1-ball to start driving
                    );   
        if(CalsAuton.useCamera){//camera-only commands 
            addCommands(new AutonRelDrvGthSht(  r, p, 3, false, false),//move out of zone for 1-ball
                        new AutonRelDrvGthSht(  r, p, 4, true, false),              //gather & dont shoot ball 2 (and ball 1 if we still have it), in front of our auton zone
                        new AutonRelDrvGthSht(  r, p, 5),              //gather & shoot ball 3 in front of allied auton zone
                        new AutonAbsDrvGthSht(  r, p, 6, true, false), //move to loading station, gather ball 5
                        new AutonAbsDrvGthSht(  r, p, 7, false, true)  //move back towards goal, shoot
                        );
        } else {//no-camera-only commands
            addCommands(new ManAutonRelDrGthSht(r, p, 3, false, 0),  //move out of zone for 1-ball
                        new ManAutonAbsDrGthSht(r, p, 4, true, 0),   //gather ball 2 (and ball 1 if we still have it), in front of our auton zone
                        new ManAutonAbsDrGthSht(r, p, 5, true, 114), //shoot ball 2 (in 2 ball only)
                        new ManAutonAbsDrGthSht(r, p, 6, true, 0),   //gather ball 3 in front of allied auton zone
                        new ManAutonAbsDrGthSht(r, p, 7, true, 166),//shoot 2 and 3
                        new ManAutonAbsDrGthSht(r, p, 8, true, 0),   //move to loading station, gather ball 4-5
                        new ManAutonGatherOnly( r, p, 9),            //gather ball 5
                        new ManAutonAbsDrGthSht(r, p, 10, true, 172)//move back towards goal, shoot
                        );
        }

        addRequirements(r.cannon, r.intake, r.drive);
    }

    @Override
    public void initialize() {
        autonStartTime = Timer.getFPGATimestamp();
        
        waitTime = r.waitTime.getSelected();

        if(CalsAuton.useCamera){
            positionList = cals.positionList[r.posChooser.getSelected()];
            todoList = cals.todoLists[r.ballCtChooser.getSelected()];
        } else {
            positionList = cals.manPositionList[r.posChooser.getSelected()];
            todoList = cals.manTodoLists[r.ballCtChooser.getSelected()];
        }
        

        /*switch(r.ballCtChooser.getSelected()){
            case 4: //3ball close
            case 6: //4ball close
            case 8: //5ball
                //if in an incompatable position then force single ball-drive auton
                //if(r.posChooser.getSelected() != 2) todoList = cals.todoLists[2]; 
            break;
        }*/

        super.initialize();
    }

    @Override
    public void end(boolean interrupted){
        super.end(interrupted);
        double totalTime = Timer.getFPGATimestamp() - autonStartTime;
        System.out.println("AUTON COMPLETED IN: " + totalTime);
        SmartDashboard.putNumber("Auton Time", totalTime);
    }

}
