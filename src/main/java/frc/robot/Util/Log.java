package frc.robot.Util;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Log extends SubsystemBase{

    public static enum LOG_GROUPS{
        INPUTS, INTAKE, DRIVE, SENSORS, SHOOTER
    }
    private static int[] VALUES = {
        4,      0,      5,     5,       0
    };

    /* 
     * All log functions take the inputted importance integer and checks it
     * against the coorisponding value to the value of the group the function 
     * belongs to. If the inputted importance integer is lower than the group
     * value, the function will output.
    */ 

    public static void logBool(boolean value, LOG_GROUPS group, int importance, boolean isSmartDashboard, String keyName){

        int enumPos = group.ordinal();

        if(importance <= VALUES[enumPos]){
            if(isSmartDashboard){
                SmartDashboard.putBoolean(keyName, value);
            } else{
                System.out.println(keyName + ":  " + value);
            }
        }
    }

    public static void logDouble(double value, LOG_GROUPS group, int importance, boolean isSmartDashboard, String keyName){

        int enumPos = group.ordinal();

        if(importance <= VALUES[enumPos]){
            if(isSmartDashboard){
                SmartDashboard.putNumber(keyName, value);
            } else{
                System.out.println(keyName + ":  " + value);
            }
        }
    }

    public static void logString(String value, LOG_GROUPS group, int importance, boolean isSmartDashboard, String keyName){

        int enumPos = group.ordinal();

        if(importance <= VALUES[enumPos]){
            if(isSmartDashboard){
                SmartDashboard.putString(keyName, value);
            } else{
                System.out.println(keyName + ":  " + value);
            }
        }
    }


    /*
     * These functions log to specific shuffleboard tabs.
     * Make all new tabs in this class.
     */
    public static ShuffleboardTab editableCalsTab;
    public static ShuffleboardTab compTab;

    public Log(){
        editableCalsTab = Shuffleboard.getTab("editable cals");
        compTab = Shuffleboard.getTab("comp");
    }

    public static void addValue(double val, String name, ShuffleboardTab tab){
        tab.add(name, val);
    }

    public static void addValue(boolean val, String name, ShuffleboardTab tab){
        tab.add(name, val);
    }

    public static void addValue(String val, String name, ShuffleboardTab tab){
        tab.add(name, val);
    }

}
