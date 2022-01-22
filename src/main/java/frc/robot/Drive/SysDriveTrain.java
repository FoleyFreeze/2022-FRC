package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Inputs.Inputs;
import frc.robot.Inputs.Sensors;
import frc.robot.Util.Vector;

public class SysDriveTrain extends SubsystemBase implements AutoCloseable {
    
    CalsDrive cals;

    public Wheel[] wheels;

    Vector centerOfRot;
    double fieldOrientOffset;
    Inputs inputs;
    Sensors sensors;

    public SysDriveTrain(CalsDrive cals){
        this.cals = cals;
        centerOfRot = cals.defaultRobotCenter;

        wheels = new Wheel[cals.wheelCals.length];
        for(int i=0; i < cals.wheelCals.length; i++){
            wheels[i] = new Wheel(cals.wheelCals[i]);
        }
    }

    private void normalize(double max){
        for(Wheel w: wheels){
            w.driveVec.r = w.driveVec.r / max;
        }
    }

    public void resetWheelEncoders(){
        for(Wheel w : wheels){
            w.resetToAbsEnc();
        }
    }

    public void driveSwerve(Vector xy, double zR){
        if(inputs.getFieldOrient()){
            xy.theta -= sensors.getFieldOrientAngle();
        }
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

        for(Wheel w : wheels){
            w.drive(allZero);
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
