package frc.robot.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public abstract class ErrorCommand extends CommandBase{
    
    SequentialCommandGroup sCG;

    public ErrorCommand(SequentialCommandGroup sCG){
        this.sCG = sCG;
    }

    @Override
    public void execute(){
        if(isError()){
            sCG.cancel();
        }
    }
    
    public boolean isError(){
        return false;
    }
}
