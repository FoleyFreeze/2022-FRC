package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Vector;

public class SysDriveTrain extends SubsystemBase {
    
    CalsDrive cals;

    Wheel[] wheels;

    public SysDriveTrain(CalsDrive cals){
        this.cals = cals;

        //TODO: create the wheels
    }


    public void driveSwerve(double x, double y, double zR){
        //create vectors
        Vector xy = Vector.fromXY(x, y);

        //create rotation vectors from wheel angle and rotation axis magnitude
        for(Wheel w : wheels){

        }


        //add vectors into wheel vectors


        //drive "swerve" motors to wheel angles
        //drive "drive" motors to wheel powers
    }


    public void periodic(){
        
    }
}
