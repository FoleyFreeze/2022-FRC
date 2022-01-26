package frc.robot.Util;

public class Interpolate {

    public static double interpolate(double[] axis, double[] table, double value){
        if(value <= axis[0]){
            return table[0];
        } else if(value > axis[axis.length-1]){
            return table[table.length-1];
        }

        int i = axis.length - 1;
        while(value < axis[--i]);//moves through all numbers in the axis array until it finds the range that value is in

        double slope = (table[i-1] - table[i]) / (axis[i-1] - axis[i]);
        double x = value - axis[i];
        return x * slope + table[i];
    }
}
