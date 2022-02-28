package frc.robot.Climber;

import frc.robot.Robot;
import frc.robot.Util.Motor.CalsMotor;
import frc.robot.Util.Motor.CalsMotor.MotorType;;

public class CalsClimb {

    public boolean DISABLED = true && Robot.isReal();

    public CalsMotor climbArms = new CalsMotor(MotorType.TALON, 3);
    public CalsMotor climbWinch = new CalsMotor(MotorType.TALON, 16);

    public CalsClimb(){

    }
}
