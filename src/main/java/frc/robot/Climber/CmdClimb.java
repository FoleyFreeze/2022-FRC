package frc.robot.Climber;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Climber.ClimbSteps.Arms;
import frc.robot.Climber.ClimbSteps.Winch;

public class CmdClimb extends SequentialCommandGroup{

    static int stage = 1;

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
        addCommands(//new Release(r, sv, 0, this), 
                    new Arms(r, sv, 1, this), new Winch(r, sv, 2, this), //bar 2
                    new Arms(r, sv, 3, this), new Winch(r, sv, 4, this), //bar 3
                    new Arms(r, sv, 5, this), new Winch(r, sv, 6, this));//bar 4 (traversal)
    }

    @Override
    public void execute(){
        super.execute();
        SmartDashboard.putNumber("climb stage", stage);
    }

    @Override
    public void end(boolean interrupted){
        super.end(interrupted);
        resetStage(!interrupted);
    }

    public static void resetStage(boolean reset){
        if(reset){
            stage = 1;
        }
    }
}
