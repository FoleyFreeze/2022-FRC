package frc.robot.Climber;

import java.util.function.DoubleConsumer;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.Climber.ClimbSteps.Arms;
import frc.robot.Climber.ClimbSteps.ManArms;
import frc.robot.Climber.ClimbSteps.Release;
import frc.robot.Climber.ClimbSteps.Winch;
import frc.robot.Util.EditableCal;

public class CmdClimb extends SequentialCommandGroup{

    public static EditableCal stageCal = new EditableCal("ClimbStage", 0);
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
        stageCal.addCallback(new DoubleConsumer() {
            public void accept(double d){
                stage = (int) d;
                System.out.println("Cal changed the climb stage to: " + stage);
            }
        });

        this.r = r;
        addCommands(new Release(r, sv, 0, this), 
                    //note that stage one is now exited with the left trigger
                    new ManArms(r, sv, 1, this), /*new WaitCommand(0.25),*/ new Winch(r, sv, 2, this), //bar 2
                    new ManArms(r, sv, 3, this), /*new WaitCommand(0.25),*/ new Winch(r, sv, 4, this), //bar 3
                    new ManArms(r, sv, 5, this), /*new WaitCommand(0.25),*/ new Winch(r, sv, 6, this));//bar 4 (traversal)
    }

    @Override
    public void initialize(){
        super.initialize();
        System.out.println();
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
        System.out.println("ClimbCmd End " + interrupted);
    }

    public static void resetStage(boolean reset){
        if(reset){
            System.out.println("Climb Stage Reset");
            stage = 0;
        }
    }
}
