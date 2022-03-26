package frc.robot.Climber;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Climber.ClimbSteps.Arms;
import frc.robot.Climber.ClimbSteps.Release;
import frc.robot.Climber.ClimbSteps.Winch;

public class CmdClimb extends SequentialCommandGroup{

    static int stage = 0;

    public class SharedVariables{
        public int get(){
            return stage;
        }
        public void set(int val){
            stage = val;
        }
    }

    SharedVariables sv = new SharedVariables();

    RobotContainer r;
    
    public CmdClimb(RobotContainer r){
        this.r = r;
        addCommands(new Release(r, sv, 0), 
                    new Arms(r, sv, 1, this), new Winch(r, sv, 2, this), //bar 1
                    new Arms(r, sv, 3, this), new Winch(r, sv, 4, this), //bar 2
                    new Arms(r, sv, 5, this), new Winch(r, sv, 6, this), //bar 3
                    new Arms(r, sv, 7, this), new Winch(r, sv, 8, this));//bar 4 (traversal)
    }

    public static void resetStage(boolean reset){
        if(reset){
            stage = 1;
        }
    }
}
