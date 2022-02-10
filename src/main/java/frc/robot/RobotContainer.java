// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Cannon.CalsCannon;
import frc.robot.Cannon.SysCannon;
import frc.robot.Climber.CalsClimb;
import frc.robot.Climber.SysClimb;
import frc.robot.Drive.CalsDrive;
import frc.robot.Drive.CmdDrive;
import frc.robot.Drive.SysDriveTrain;
import frc.robot.Inputs.CalsInputs;
import frc.robot.Inputs.Inputs;
import frc.robot.Intake.CalsIntake;
import frc.robot.Intake.SysIntake;
import frc.robot.Sensors.CalsSensors;
import frc.robot.Sensors.Sensors;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer implements AutoCloseable{
  // The robot's subsystems and commands are defined here...

  public final Inputs inputs = new Inputs(new CalsInputs());
  public final SysCannon cannon = new SysCannon(new CalsCannon());
  public final SysDriveTrain drive = new SysDriveTrain(new CalsDrive(), this);
  public final SysIntake intake = new SysIntake(new CalsIntake());
  public final SysClimb climb = new SysClimb(new CalsClimb());
  public final Sensors sensors = new Sensors(new CalsSensors(),this); //needs to run after drive is created


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
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

    inputs.resetSwerveAngles.whileActiveOnce(new InstantCommand(drive::learnWheelAngs));
    inputs.resetNavXAng.whileActiveOnce(new InstantCommand(sensors::resetAng));
    inputs.resetNavXPos.whileActiveOnce(new InstantCommand(sensors::resetPos));

    //inputs.primeCannon.whileActiveContinuous(new InstantCommand(cannon::prime));
    //inputs.fireCannon.whileActiveContinuous(new InstantCommand(cannon::fire));

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
}
