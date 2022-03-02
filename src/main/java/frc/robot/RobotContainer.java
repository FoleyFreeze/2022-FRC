// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Cannon.CalsCannon;
import frc.robot.Cannon.CmdLoadSequential;
import frc.robot.Cannon.CmdCannonAngleReset;
import frc.robot.Cannon.CmdShoot;
import frc.robot.Cannon.SysCannon;
import frc.robot.Climber.CalsClimb;
import frc.robot.Climber.SysClimb;
import frc.robot.Drive.CalsDrive;
import frc.robot.Drive.CmdDrive;
import frc.robot.Drive.SysDriveTrain;
import frc.robot.Inputs.CalsCBoard;
import frc.robot.Inputs.CalsFlysky;
import frc.robot.Inputs.CalsInputs;
import frc.robot.Inputs.Inputs;
import frc.robot.Intake.CalsIntake;
import frc.robot.Intake.CmdGather;
import frc.robot.Intake.SysIntake;
import frc.robot.Sensors.CalsSensors;
import frc.robot.Sensors.Sensors;
import frc.robot.Util.InstantAlwaysCommand;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer implements AutoCloseable{
  // The robot's subsystems and commands are defined here...

  public final Inputs inputs;
  public final SysCannon cannon;
  public final SysDriveTrain drive;
  public final SysIntake intake;
  public final SysClimb climb;
  public final Sensors sensors;


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    inputs = new Inputs(new CalsInputs(), new CalsFlysky(), new CalsCBoard());
    drive = new SysDriveTrain(new CalsDrive(), this);
    intake = new SysIntake(new CalsIntake());
    climb = new SysClimb(new CalsClimb());
    cannon = new SysCannon(new CalsCannon(), this);
    sensors = new Sensors(new CalsSensors(), this); //needs to run after drive is created
    

    CommandScheduler cs = CommandScheduler.getInstance();
    cs.registerSubsystem(inputs, intake, cannon, drive); //order matters
    cs.setDefaultCommand(drive, new CmdDrive(this));

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    inputs.resetSwerveAngles.whileActiveOnce(new InstantAlwaysCommand(drive::learnWheelAngs));
    inputs.resetNavXAng.whileActiveOnce(new InstantAlwaysCommand(sensors::resetAng));
    inputs.resetNavXPos.whileActiveOnce(new InstantAlwaysCommand(sensors::resetPos));
      
    inputs.loadCargo.whileActiveOnce(new CmdLoadSequential(this));
    inputs.fireCannon.whileActiveOnce(new CmdShoot(this));
    inputs.resetCannon.whileActiveOnce(new CmdCannonAngleReset(this));

    inputs.intake.whileActiveOnce(new CmdGather(this));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return null;
  }

  @Override
  public void close() throws Exception {
    inputs.close();
    cannon.close();
    drive.close();
    intake.close();
    climb.close();
    sensors.close();
  }

    //this is not relevant to anything delete if needed
    public boolean monkeyTrouble(boolean aSmile, boolean bSmile){
      return (aSmile == bSmile);
    }
  
}
