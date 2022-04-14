package frc.robot.Drive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Angle;
import frc.robot.Util.FileManager;
import frc.robot.Util.Log;
import frc.robot.Util.Vector;

public class SysDriveTrain extends SubsystemBase implements AutoCloseable {
    
    RobotContainer r;
    public CalsDrive cals;
    Inputs inputs;

    public Wheel[] wheels;

    Vector centerOfRot;
    double fieldOrientOffset;
    FileManager fm = new FileManager("/home/lvuser/WheelEncoderOffsets.txt");

    public double angerror;

    public SysDriveTrain(CalsDrive cals, RobotContainer r){
        this.r = r;
        this.cals = cals;
        inputs = r.inputs;
        if(cals.DISABLED) return;
        centerOfRot = cals.defaultRobotCenter;

        wheels = new Wheel[cals.wheelCals.length];
        for(int i=0; i < cals.wheelCals.length; i++){
            wheels[i] = new Wheel(cals.wheelCals[i], r);
        }

        try{
            if(fm.exists()){
                System.out.println("Reading wheel positions file:");
                for(Wheel w : wheels) {
                    double val = Double.parseDouble(fm.readLine());
                    System.out.println(w.cals.name + " " + val);
                    w.rawAbsEncOffset = val;
                }

                fm.close();
                }
        }catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            //if there was an error, reset to cal value
            System.out.println("Error reading file, defaulting to:");
            for(Wheel w : wheels) {
                w.rawAbsEncOffset = w.cals.angleEncoderOffset;
                System.out.println(w.cals.name + " " + w.rawAbsEncOffset);
            }
        }

        resetWheelEncoders();
    }

    public void learnWheelAngs(){
        if(cals.DISABLED) return;

        for(Wheel w : wheels) {
            w.learnWheelAngle();
        }

        try{
            System.out.println("Saving new wheel locations:");
            for(Wheel w : wheels) {
                fm.writeLine(Double.toString(w.rawAbsEncOffset));
                System.out.println(w.cals.name + " " + w.rawAbsEncOffset);
            }

            fm.close();
        }catch(Exception e){
            System.out.println("Error while saving wheel locations:");
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void normalize(double max){
        if(cals.DISABLED) return;

        for(Wheel w: wheels){
            w.driveVec.r = w.driveVec.r / max;
        }
    }

    public void resetWheelEncoders(){
        if(cals.DISABLED) return;

        for(Wheel w : wheels){
            w.resetToAbsEnc();
        }
    }

    public void driveSwerveAng(Vector xy, double tgtAng, double maxPwr, double kR, double kD){
        driveSwerveAng(xy, tgtAng, maxPwr, kR, kD, null);
    }

    public void driveSwerveAng(Vector xy, double tgtAng, double maxPwr, double kR, double kD, Vector centerOfStrafe){
        if(cals.DISABLED) return;

        angerror = Angle.normDeg(tgtAng - r.sensors.botAng);
        SmartDashboard.putNumber("RotAngleErr", angerror);

        if(Math.abs(xy.r) > 0.05) kD = 0;//no d while moving, too much noise
        double zR = angerror * kR + r.sensors.dBotAng * kD;
        if(zR > maxPwr) zR = maxPwr;
        else if(zR < -maxPwr) zR = -maxPwr;

        driveSwerve(xy, zR, centerOfStrafe);
    }

    public void driveSwerve(Vector xy, double zR){
        driveSwerve(xy, zR, null);
    }

    boolean prevSwState = false;
    double climbTimeStart = -r.climb.cals.alignTime;
    //note that centerOfStrafe is robot relative
    public void driveSwerve(Vector xy, double zR, Vector centerOfStrafe){
        if(cals.DISABLED) return;
        
        if(inputs.getFieldOrient()){
            xy.theta -= Math.toRadians(r.sensors.botAng);
        }

        if(inputs.operatorJoy.climbSwitch() && !prevSwState && r.sensors.navX.navX.isConnected()){
            climbTimeStart = Timer.getFPGATimestamp();
        }
        prevSwState = inputs.operatorJoy.climbSwitch();

        if(climbTimeStart + r.climb.cals.alignTime > Timer.getFPGATimestamp()){
            //if climbing, lock orienttion towards drivers station (-90°)
            double error = Angle.normDeg(180 - r.sensors.botAng);

            zR = error * cals.climbAngleKp;
        }

        if(centerOfStrafe != null){
            double newXaxisAng = Angle.normRad(centerOfStrafe.theta - Math.PI/2);
            //System.out.println(Math.toDegrees(newXaxisAng));
            //System.out.println("1: " + xy.toStringXY());
            
            //we can modify xy here since its not used again
            xy.theta = Angle.normRad(xy.theta - newXaxisAng);
            Vector y = new Vector(xy.getY(), Math.PI/2 + newXaxisAng);
            //System.out.println("2: " + xy.toStringXY());

            double maxRotMag = 0;
            for(Wheel w : wheels){
                Vector v = w.calcRotAngle(centerOfStrafe);
                if(Math.abs(v.r) > maxRotMag) maxRotMag = Math.abs(v.r);
                w.driveVec = v;
            }
            
            for(Wheel w : wheels){
                w.driveVec.r /= maxRotMag;
                //apply the "x" power along the rotation axis
                w.driveVec.r *= xy.getX();
                //apply the "y" power straight ahead with no rotation portion
                w.driveVec.add(y);
            }
            //System.out.println(wheels[0].driveVec.toStringXY() + " " + wheels[1].driveVec.toStringXY() + " " + wheels[2].driveVec.toStringXY() + " " + wheels[3].driveVec.toStringXY());

        } else {
            for(Wheel w : wheels){
                w.driveVec = new Vector(xy);
            }
        }

        //create rotation vectors from wheel angle and rotation axis magnitude
        double max = 0;
        boolean allZero = true;
        for(Wheel w : wheels){
            Vector rotateVec = w.calcRotAngle(centerOfRot);
            //note that this will need normalization if we ever use a center of rotation that is not equidistant from each wheel
            Vector v = new Vector(zR, rotateVec.theta);

            //add vectors into wheel vectors
            w.driveVec.add(v);

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
        if(cals.DISABLED) return;

        for(Wheel w : wheels){
            w.periodic();
        }

        Log.addValue(String.format("%d, %d, %d, %d",
                                   wheels[0].errorCount, wheels[1].errorCount,
                                   wheels[2].errorCount, wheels[3].errorCount),
                                   "Swerve Error #", Log.compTab);

        double maxDriveTemp = 0;
        double maxSwerveTemp = 0;
        for(Wheel w : wheels){
            double t = w.getDriveTemp();
            if(t > maxDriveTemp) maxDriveTemp = t;
            t = w.getSwerveTemp();
            if(t > maxSwerveTemp) maxSwerveTemp = t;
        }

        Log.addValue(maxDriveTemp, "DriveTemp", Log.compTab);
        Log.addValue(maxSwerveTemp, "SwerveTemp", Log.compTab);
    }

    @Override
    public void close() throws Exception {
        for(Wheel w : wheels){
            w.close();
        }
    }
}
