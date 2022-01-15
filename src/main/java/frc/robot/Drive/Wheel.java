package frc.robot.Drive;

import frc.robot.Util.Motor;
import frc.robot.Util.Vector;

public class Wheel {
    
    Motor drive; //wheel power motor
    Motor swerve; //wheel angle motor

    public Vector wheelLocation;

    public Wheel(){
        //assign motors and wheel position
    }

    public double calcRotAngle(Vector centerOfRot){
        Vector v = Vector.subVectors(wheelLocation, centerOfRot);

        return v.theta + (Math.PI / 2);
    }

}
