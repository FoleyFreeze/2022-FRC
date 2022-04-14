// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Auton.AutonSequential;
import frc.robot.Auton.CalsAuton;
import frc.robot.Cannon.CalsCannon;
import frc.robot.Cannon.CmdLoadSequential;
import frc.robot.Cannon.CmdCannonEasyReset;
import frc.robot.Cannon.CmdShoot;
import frc.robot.Cannon.SysCannon;
import frc.robot.Climber.CalsClimb;
import frc.robot.Climber.CmdClimb;
import frc.robot.Climber.CmdManualClimb;
import frc.robot.Climber.SysClimb;
import frc.robot.Drive.CalsDrive;
import frc.robot.Drive.CmdDrive;
import frc.robot.Drive.SysDriveTrain;
import frc.robot.Inputs.CalsCBoard;
import frc.robot.Inputs.CalsFlysky;
import frc.robot.Inputs.CalsInputs;
import frc.robot.Inputs.Inputs;
import frc.robot.Intake.CalsIntake;
import frc.robot.Intake.CmdAutoGather;
import frc.robot.Intake.CmdGatherManual;
import frc.robot.Intake.SysIntake;
import frc.robot.Sensors.CalsSensors;
import frc.robot.Sensors.Sensors;
import frc.robot.Util.InstantAlwaysCommand;
import frc.robot.Util.Log;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer implements AutoCloseable{
  // The robot's subsystems and commands are defined here...

  public final Log log;
  public final Inputs inputs;
  public final SysCannon cannon;
  public final SysDriveTrain drive;
  public final SysIntake intake;
  public final SysClimb climb;
  public final Sensors sensors;

  public SendableChooser<Integer> posChooser;
  public SendableChooser<Integer> ballCtChooser;
  public SendableChooser<Double> waitTime;


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    log = new Log();
    inputs = new Inputs(new CalsInputs(), new CalsFlysky(), new CalsCBoard(), this);
    drive = new SysDriveTrain(new CalsDrive(), this);
    intake = new SysIntake(new CalsIntake());
    climb = new SysClimb(new CalsClimb(), this);
    cannon = new SysCannon(new CalsCannon(), this);
    sensors = new Sensors(new CalsSensors(), this); //needs to run after drive is created
    

    CommandScheduler cs = CommandScheduler.getInstance();
    cs.registerSubsystem(inputs, intake, cannon, drive); //order matters
    cs.setDefaultCommand(drive, new CmdDrive(this));

    // Configure the button bindings
    configureButtonBindings();

    posChooser = new SendableChooser<>();
    posChooser.addOption("Left", 0);
    posChooser.addOption("Mid", 1);
    posChooser.addOption("Right", 2);
    posChooser.addOption("Actually Mid", 3);

    ballCtChooser = new SendableChooser<>();
    ballCtChooser.addOption("nothing!", 0);
    ballCtChooser.addOption("1-ball", 1);
    ballCtChooser.addOption("1-ball-drive", 2);
    ballCtChooser.addOption("2-ball", 3);
    ballCtChooser.addOption("3-ball right", 4);
    ballCtChooser.addOption("3-ball left", 5);
    ballCtChooser.addOption("4-ball left", 6);
    ballCtChooser.addOption("4-ball far", 7);
    ballCtChooser.addOption("5-ball", 8);

    waitTime = new SendableChooser<>();
    waitTime.addOption("0 seconds", 0.0);
    waitTime.addOption("1 second", 1.0);
    waitTime.addOption("2 seconds", 2.0);
    waitTime.addOption("3 seconds", 3.0);
    waitTime.addOption("4 seconds", 4.0);
    waitTime.addOption("5 seconds", 5.0);
    waitTime.addOption("6 seconds", 6.0);

    Log.compTab.add(posChooser);
    Log.compTab.add(ballCtChooser);
    Log.compTab.add(waitTime);
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
    inputs.resetClimberEnc.whileActiveOnce(new InstantAlwaysCommand(climb::resetAllClimbEncoders));
      
    inputs.loadCargo.whileActiveOnce(new CmdLoadSequential(this));
    inputs.fireCannon.and(inputs.getClimbMode.negate()).whileActiveOnce(new CmdShoot(this));
    //inputs.resetCannon.whileActiveOnce(new CmdCannonAngleReset(this));
    //inputs.sensorResetCannon.whenActive(new CmdCannonSensorReset(this));
    inputs.sensorResetCannon.whenActive(new CmdCannonEasyReset(this));

    inputs.jogUp.whenActive(new InstantAlwaysCommand(cannon::jogSpeedUp));
    inputs.jogDn.whenActive(new InstantAlwaysCommand(cannon::jogSpeedDn));
    inputs.jogLeft.whenActive(new InstantAlwaysCommand(cannon::jogAngBack));
    inputs.jogRight.whenActive(new InstantAlwaysCommand(cannon::jogAngFwd));

    //inputs.gather.and(inputs.manualGather.negate()).and(inputs.autoGather.negate()).whileActiveOnce(new CmdGather(this));
    inputs.manualGather.whileActiveOnce(new CmdGatherManual(this));
    inputs.autoGather.and(inputs.gather).and(inputs.getClimbMode.negate()).whileActiveOnce(new CmdAutoGather(this));

    inputs.fireCannon.and(inputs.getClimbMode).and(inputs.manualClimber.negate()).whileActiveOnce(new CmdClimb(this));
    inputs.fireCannon.and(inputs.manualClimber).whileActiveOnce(new CmdManualClimb(this));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return new AutonSequential(this, new CalsAuton());
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
