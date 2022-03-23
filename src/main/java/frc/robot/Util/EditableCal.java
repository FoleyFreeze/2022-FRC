package frc.robot.Util;

import java.util.ArrayList;
import java.util.function.DoubleConsumer;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EditableCal {

    public static ArrayList<EditableCal> allCals = new ArrayList<>();

    public static void periodic(){
        for (EditableCal c : allCals) {
            c.check();
        }
    }
    
    public double cal;
    public String name;
    public ArrayList<DoubleConsumer> callbacks = new ArrayList<>();

    boolean enabled = true;

    public EditableCal(String nombre, double value){
        cal = value;
        name = nombre;

        SmartDashboard.putNumber(nombre, value);

        allCals.add(this);
    }

    public EditableCal(String nombre, double value, boolean enabled){
        cal = value;
        name = nombre;
        if(enabled){
            SmartDashboard.putNumber(nombre, value);
        }
        this.enabled = enabled;

        allCals.add(this);
    }

    public double get(){
        return cal;
    }

    public void check(){
        if(!enabled) return;
        double value = SmartDashboard.getNumber(name, cal);
        if (value != cal){
            cal = value;

            for (DoubleConsumer dc : callbacks) {
                dc.accept(value);
            }
        }
    }

    public void addCallback(DoubleConsumer callMeMaybe){
        if(!enabled) return;
        callbacks.add(callMeMaybe);
    }
}
