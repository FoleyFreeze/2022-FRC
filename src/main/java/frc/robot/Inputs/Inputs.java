package frc.robot.Inputs;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Util;

public class Inputs extends SubsystemBase{

    CalsInputs cals;
    public Joystick controller;

    public Inputs(CalsInputs cals){
        this.cals = cals;
    }

    public void isGamePieceDetected(){

    }

    public void periodic(){
        
    }
}