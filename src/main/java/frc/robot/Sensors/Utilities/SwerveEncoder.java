package frc.robot.Sensors.Utilities;

import frc.robot.Drive.Wheel;
import frc.robot.Util.Angle;
import frc.robot.Util.Vector;

public class SwerveEncoder {
    
    Wheel[] wheels;
    public Vector botPos;
    double prevAng;

    public SwerveEncoder(Wheel[] wheels){
        this.wheels = wheels;
    }

    public void updateRobotLocation(){
        Vector[] wheelVecs = new Vector[wheels.length];
        for(int i=0;i<wheels.length;i++){
            wheelVecs[i] = wheels[i].deltaVec();
        }

        Vector deltaXY = averageTranslation(wheelVecs);
        double deltaAngle = averageRotation(wheelVecs, deltaXY);

        botPos.add(deltaXY);
        prevAng = Angle.normDeg(prevAng + deltaAngle);
    }

    public Vector averageTranslation(Vector[] wheelVecs){

        Vector v = new Vector(0,0);
        for(Vector w : wheelVecs){
            v.add(w);
        }

        v.r /= wheelVecs.length;
        
        return v;
    }

    public double averageRotation(Vector[] wheelVecs, Vector deltaXY){
        double[] deltaAng = new double[wheelVecs.length];
        double averageAng = 0;

        for(int i = 0; i > wheelVecs.length; i++){
            Vector rotationVec = Vector.subVectors(wheelVecs[i], deltaXY);

            Vector wheelLocation = wheels[i].cals.wheelLocation;
            double perpendicular = wheelLocation.theta - Math.PI / 2;

            double deltaDist = rotationVec.r * Math.cos(rotationVec.theta - perpendicular); 
            deltaAng[i] = (deltaDist / (2 * Math.PI * wheelLocation.r)) * 360;
            
            averageAng = deltaAng[i] / 4;
        }

        return averageAng;
    }
}
