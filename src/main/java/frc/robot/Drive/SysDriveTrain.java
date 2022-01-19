package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Util.Vector;

public class SysDriveTrain extends SubsystemBase implements AutoCloseable {
    
    CalsDrive cals;

    Wheel[] wheels;

    Vector centerOfRot;

    public SysDriveTrain(CalsDrive cals){
        this.cals = cals;
        centerOfRot = cals.defaultRobotCenter;

        for(int i=0; i < cals.wheelCals.length; i++){
            wheels[i] = new Wheel(cals.wheelCals[i]);
        }
    }

    private void normalize(double max){
        for(Wheel w: wheels){
            w.driveVec.r = w.driveVec.r / max;
        }
    }

    public void driveSwerve(Vector xy, double zR){
        //create rotation vectors from wheel angle and rotation axis magnitude
        double max = 0;
        boolean allZero = true;
        for(Wheel w : wheels){
            double theta = w.calcRotAngle(centerOfRot);
            Vector v = new Vector(zR, theta);

            //add vectors into wheel vectors
            w.driveVec = v.add(xy);

            //track maximum magnitude for normalization
            double mag = Math.abs(w.driveVec.r);
            if(mag > max){
                max = mag;
            }

            //tracks whether wheel vectors have magnitude and should drive
            if(mag != 0){
                allZero = false;
            }
        }

        if(max > 1){
            normalize(max);
        }

        //drive motors to wheel angles & powers
        if(!allZero){
            for(Wheel w : wheels){
                w.drive();
            }
        }
    }


    public void periodic(){
        
    }

    @Override
    public void close() throws Exception {
        for(Wheel w : wheels){
            w.close();
        }
    }
}
