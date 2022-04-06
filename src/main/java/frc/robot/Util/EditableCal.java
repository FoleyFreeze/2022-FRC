package frc.robot.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.DoubleConsumer;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;

public class EditableCal {

    public static final EditableCal ZERO = new EditableCal("zero", 0, false);

    public static ArrayList<EditableCal> allCals = new ArrayList<>();

    public static ShuffleboardTab calsTab = Shuffleboard.getTab("Cals");
    public static HashMap<String,SimpleWidget> map = new HashMap<>();

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

        //Log.addValue(value, nombre, Log.editableCalsTab);
        SimpleWidget w = calsTab.add(nombre, value);
        map.put(nombre, w);

        allCals.add(this);
    }

    public EditableCal(String nombre, double value, boolean enabled){
        cal = value;
        name = nombre;
        if(enabled){
            //Log.addValue(value, nombre, Log.editableCalsTab);
            SimpleWidget w = calsTab.add(nombre, value);
            map.put(nombre, w);

            allCals.add(this);
        }
    }

    public double get(){
        return cal;
    }

    public int getAsInt(){
        return (int) (cal + 0.5);
    }

    //sets the cal value to an inputted number on the dashboard
    public void check(){
        //double value = SmartDashboard.getNumber(name, cal);
        double value = map.get(name).getEntry().getNumber(0).doubleValue();

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
