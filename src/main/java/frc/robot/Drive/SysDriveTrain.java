package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Inputs.Inputs;
import frc.robot.Util.Angle;
import frc.robot.Util.FileManager;
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
            wheels[i] = new Wheel(cals.wheelCals[i]);
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
        if(cals.DISABLED) return;
        
        angerror = Angle.normDeg(tgtAng - r.sensors.botAng);
        double zR = angerror * kR;
        if(zR > maxPwr) zR = maxPwr;

        driveSwerve(xy, zR);
    }

    public void driveSwerve(Vector xy, double zR){
        if(cals.DISABLED) return;
        
        if(inputs.getFieldOrient()){
            xy.theta -= Math.toRadians(r.sensors.botAng);
        }

        /*if(inputs.operatorJoy.climbSwitch() && r.sensors.navX.navX.isConnected()){
            //if climbing, lock orienttion towards drivers station (-90°)
            double error = Angle.normDeg(180 - r.sensors.botAng);

            zR = error * cals.climbAngleKp;
        }*/
        //TODO: do we want to lock the angle for a climb?
        //do logic elsewhere w/ power control

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
        if(cals.DISABLED) return;
    }

    @Override
    public void close() throws Exception {
        for(Wheel w : wheels){
            w.close();
        }
    }
}
