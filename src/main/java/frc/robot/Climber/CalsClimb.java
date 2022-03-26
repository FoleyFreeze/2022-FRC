package frc.robot.Climber;

import frc.robot.Robot;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;;

public class CalsClimb {

    public boolean DISABLED = false && Robot.isReal();

    public CalsMotor climbArms = new CalsMotor(MotorType.TALON, 3);
    public CalsMotor climbWinch = new CalsMotor(MotorType.TALON, 16);

    public double armPower = 0;
    public double winchPower = 0;

    //arms command cals
    public double posCheckDelayArms = 3;
    public double minRotDiffArms = 0;
    public double extraTimeArms = 0.25;
    public double maxRunTimeArms = 10;

    //winch command cals
    public double posCheckDelayWinch = 3;
    public double minRotDiffWinch = 0;
    public double extraTimeWinch = 0.25;
    public double maxRunTimeWinch = 10;

    //release command cals

    public CalsClimb(){

    }
}
