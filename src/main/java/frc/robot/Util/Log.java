package frc.robot.Util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Log {

    public static enum LOG_GROUPS{
        INPUTS, INTAKE, DRIVE, VISION, SHOOTER
    }
    private static int[] VALUES = {
        5,      0,      0,     0,      0
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
}
