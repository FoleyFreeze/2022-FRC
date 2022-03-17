package frc.robot.Util;

import java.util.ArrayList;

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

    public EditableCal(String nombre, double value){
        cal = value;
        name = nombre;

        SmartDashboard.getNumber(nombre, value);

        allCals.add(this);
    } 

    public double get(){
        return cal;
    }

    public void check(){
        double value = SmartDashboard.getNumber(name, cal);
        if (value != cal){
            cal = value;

        }
            
    }
}
