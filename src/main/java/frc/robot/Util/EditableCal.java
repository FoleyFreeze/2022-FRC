package frc.robot.Util;

import java.util.ArrayList;
import java.util.function.DoubleConsumer;

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

    public EditableCal(String nombre, double value){
        cal = value;
        name = nombre;


        SmartDashboard.putNumber(nombre, value);

        allCals.add(this);
    } 

    public double get(){
        return cal;
    }

    public void check(){
        double value = SmartDashboard.getNumber(name, cal);
        if (value != cal){
            cal = value;

            for (DoubleConsumer dc : callbacks) {
                dc.accept(value);
            }
        }
            
    }

    public void addCallback(DoubleConsumer callMeMaybe){
        callbacks.add(callMeMaybe);
    }
}
