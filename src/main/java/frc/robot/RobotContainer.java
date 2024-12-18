package frc.robot;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.simulation.JoystickSim;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Commands.aimVision;
import frc.robot.Commands.autoIntake;
import frc.robot.Commands.rotateTo;
import frc.robot.Commands.setArm;
import frc.robot.Commands.setArmTo;
import frc.robot.Commands.setClimber;
import frc.robot.Commands.setHead;
import frc.robot.Commands.setHeadTo;
import frc.robot.Commands.setIntake;
import frc.robot.Commands.setShooter;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.HeadSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.limitSwitchSubsystem;

public class RobotContainer 
{
  private double MaxSpeed = TunerConstants.kSpeedAt12VoltsMps; // kSpeedAt12VoltsMps desired top speed
  private double MaxAngularRate = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity

  /* Setting up bindings for necessary control of the swerve drive platform */
  private final CommandXboxController joystick = new CommandXboxController(0); // My joystick
  private final XboxController coDriver = new XboxController(1); // My co-joystick
  private final XboxController Driver = new XboxController(0); //Driver Buttons
  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
    .withDeadband(MaxSpeed * 0.06).withRotationalDeadband(MaxAngularRate * 0.07) // Add a 10% deadband
    .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric
                                                               // driving in open loop
  
  // driver buttons
  private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
  private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
  private final JoystickButton armOverrideButton = new JoystickButton(Driver, XboxController.Button.kStart.value);
  private final JoystickButton climberUp = new JoystickButton(Driver, XboxController.Button.kX.value);
  private final JoystickButton climberDown = new JoystickButton(Driver, XboxController.Button.kB.value);
  private final JoystickButton slowShootButton = new JoystickButton(Driver, XboxController.Button.kRightBumper.value);
  private final JoystickButton servoBombButton = new JoystickButton(Driver, XboxController.Button.kLeftBumper.value);
  private final JoystickButton trapButton = new JoystickButton(Driver, XboxController.Button.kBack.value);

  // co driver buttons
  private final JoystickButton intakeButton = new JoystickButton(coDriver, XboxController.Button.kLeftBumper.value);
  private final JoystickButton setLineButton = new JoystickButton(coDriver, XboxController.Button.kA.value);
  private final JoystickButton reverseIntake = new JoystickButton(coDriver, XboxController.Button.kStart.value);
  private final JoystickButton setAmpButton = new JoystickButton(coDriver, XboxController.Button.kX.value);
  private final JoystickButton setShootButton = new JoystickButton(coDriver, XboxController.Button.kRightBumper.value);
  private final JoystickButton trapButton2 = new JoystickButton(coDriver, XboxController.Button.kBack.value);
  private final JoystickButton setSafeButton = new JoystickButton(coDriver, XboxController.Button.kY.value);
  private final JoystickButton vARSHOOTTrigger = new JoystickButton(coDriver, XboxController.Button.kLeftStick.value);

  /* Subsystems */
  public final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain; // My drivetrain
  private final ArmSubsystem m_arm = new ArmSubsystem(); // My arm
  private final IntakeSubsystem m_intake = new IntakeSubsystem(); // My intake
  private final ShooterSubsystem m_shooter = new ShooterSubsystem(); // My shooter
  private final ClimberSubsystem m_climber = new ClimberSubsystem();// My Climber
  private final limitSwitchSubsystem m_switch = new limitSwitchSubsystem(); // My limit switch
  public final HeadSubsystem m_head = new HeadSubsystem(); // My head
  public final CommandSwerveDrivetrain vision_drivetrain = TunerConstants.DriveTrain; // My vision drive train

  /* Auto List */
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  private void configureBindings() 
  {
    // m_vision.periodic(); // start vision
    m_arm.setDefaultCommand(
            new setArm(() -> coDriver.getRawAxis(XboxController.Axis.kLeftY.value), m_arm)); // this is how you get the left stick y value and use it
    m_head.setDefaultCommand(
            new setHead(() -> coDriver.getRawAxis(XboxController.Axis.kRightY.value), m_head));
    drivetrain.setDefaultCommand
    (
      drivetrain.applyRequest(() -> drive.withVelocityX(MathUtil.applyDeadband(-joystick.getLeftY(), 0.01) * MaxSpeed)
      .withVelocityY(MathUtil.applyDeadband(-joystick.getLeftX(), 0.01) * MaxSpeed) // Drive left with negative X (left)
      .withRotationalRate(MathUtil.applyDeadband(-joystick.getRightX(), 0.01) * MaxAngularRate) // Drive counterclockwise with negative X (left)
      ).ignoringDisable(true));

    joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
    

    // reset the field-centric heading on left bumper press
    joystick.y().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldRelative()));
    joystick.pov(0).whileTrue(drivetrain.applyRequest(() -> forwardStraight.withVelocityX(0.5).withVelocityY(0)));
    joystick.pov(180).whileTrue(drivetrain.applyRequest(() -> forwardStraight.withVelocityX(-0.5).withVelocityY(0)));

    intakeButton.whileTrue(new setIntake(Constants.Intake.Speed, m_intake));
    intakeButton.onFalse(new ParallelCommandGroup(new setIntake((-Constants.Intake.Speed * 0.25), m_intake).withTimeout(0.2), new setShooter((-Constants.Shooter.shooterSpeed * .1), m_shooter).withTimeout(0.2)));
    reverseIntake.whileTrue(new ParallelCommandGroup(new setIntake((-Constants.Intake.Speed * 0.25), m_intake), new setShooter((-Constants.Shooter.shooterSpeed * .1), m_shooter)));
    setSafeButton.whileTrue(new ParallelCommandGroup(new aimVision(Constants.Aiming.getTag(), vision_drivetrain), new setArmTo(Constants.Aiming.safeArm , m_arm, "Line Shot"), new setHeadTo(Constants.Aiming.safeHead, m_head, "Line Shot"), new setShooter(Constants.Shooter.shooterSpeed, m_shooter)));
    setSafeButton.onFalse(new ParallelCommandGroup(new setArmTo(Constants.Aiming.Home, m_arm, "Home").withTimeout(3), new setHeadTo(Constants.Aiming.Home2, m_head, "Home 2").withTimeout(3)));
    setAmpButton.whileTrue(new ParallelCommandGroup(new setArmTo(Constants.Aiming.Amp , m_arm, "Amp"), new setHeadTo(Constants.Aiming.Amp2, m_head, "Amp2"), new setShooter((Constants.Shooter.shooterSpeed * 0.2), m_shooter)));
    setAmpButton.onFalse(new ParallelCommandGroup(new setArmTo(Constants.Aiming.Home, m_arm, "Home").withTimeout(3), new setHeadTo(Constants.Aiming.Home2, m_head, "Home 2").withTimeout(3)));
    setShootButton.whileTrue(new ParallelCommandGroup(new setHeadTo(Constants.Aiming.Upclose, m_head, "Upclose"), new setShooter(Constants.Shooter.shooterSpeed, m_shooter)));
    setShootButton.onFalse(new ParallelCommandGroup(new setArmTo(Constants.Aiming.Home, m_arm, "Home").withTimeout(3), new setHeadTo(Constants.Aiming.Home2, m_head, "Home 2").withTimeout(3)));
    armOverrideButton.whileFalse(new InstantCommand(() -> m_climber.setOverride(false)));
    armOverrideButton.whileTrue(new InstantCommand(() -> m_climber.setOverride(true)));
    climberUp.whileTrue(new setClimber(Constants.Climber.climberUp, m_climber));
    climberDown.whileTrue(new setClimber(Constants.Climber.climberDown, m_climber));
    slowShootButton.whileTrue(new InstantCommand(() -> m_shooter.setSlow(true)));
    slowShootButton.whileFalse(new InstantCommand(() -> m_shooter.setSlow(false)));
    servoBombButton.whileTrue(new InstantCommand(() -> m_head.ServoBomb()));
    servoBombButton.whileFalse(new InstantCommand(() -> m_head.ServoHome()));
    trapButton.whileTrue(new ParallelCommandGroup(new setArmTo(Constants.Trap.hell, m_arm, "hell"), new setHeadTo(Constants.Trap.heaven, m_head, "heaven"), new setShooter(0.2, m_shooter)));
    trapButton2.whileTrue(new ParallelCommandGroup(new setArmTo(Constants.Trap.hell, m_arm, "hell"), new setHeadTo(Constants.Trap.heaven, m_head, "heaven"), new setShooter(0.2, m_shooter)));
    setLineButton.whileTrue(new ParallelCommandGroup(new aimVision(Constants.Aiming.getTag(), vision_drivetrain), new setHeadTo(Constants.Aiming.lineHead, m_head, "Line Shot"), new setShooter(Constants.Shooter.shooterSpeed, m_shooter)));
    setLineButton.onFalse(new ParallelCommandGroup(new setArmTo(Constants.Aiming.Home, m_arm, "Home").withTimeout(3), new setHeadTo(Constants.Aiming.Home2, m_head, "Home 2").withTimeout(3)));
    vARSHOOTTrigger.whileTrue(new ParallelCommandGroup (new setShooter(XboxController.Axis.kRightTrigger.value, m_shooter), new setArmTo(Constants.Aiming.farArm, m_arm, "WEEEEE"), new setHeadTo(Constants.Aiming.kindaUpClose, m_head, null)));
  }

  public RobotContainer()
  {
    NamedCommands.registerCommand("intakept1", new setIntake(Constants.Intake.Speed, m_intake));
    NamedCommands.registerCommand("intakept2", new ParallelCommandGroup(new setIntake((-Constants.Intake.Speed * 0.25), m_intake).withTimeout(0.2), new setShooter((-Constants.Shooter.shooterSpeed * .1), m_shooter).withTimeout(0.2)));
    NamedCommands.registerCommand("reverseintake", new setIntake(-Constants.Intake.Speed * 0.25, m_intake));
    NamedCommands.registerCommand("setShootUpClose", new setHeadTo(Constants.Aiming.Upclose, m_head, "Upclose"));
    NamedCommands.registerCommand("setShootLineHead", new setHeadTo(Constants.Aiming.lineHead, m_head, "Line"));
    NamedCommands.registerCommand("Pickup2ShootPosition", new setHeadTo(Constants.Aiming.kindaUpClose, m_head, "PickupShootHead"));
    NamedCommands.registerCommand("setShootLineArm", new setArmTo(Constants.Aiming.lineArm, m_arm, "Line"));
    NamedCommands.registerCommand("setShootMotor", new setShooter(Constants.Shooter.shooterSpeed, m_shooter));
    NamedCommands.registerCommand("setIntake", new setIntake(Constants.Intake.Speed, m_intake));
    NamedCommands.registerCommand("setHome", new ParallelCommandGroup(new setArmTo(Constants.Aiming.Home, m_arm, "Home").withTimeout(1), new setHeadTo(Constants.Aiming.Home2, m_head, "Home 2").withTimeout(1)));
    NamedCommands.registerCommand("setAmp", new ParallelCommandGroup(new setArmTo(Constants.Aiming.Amp , m_arm, "Amp"), new setHeadTo(Constants.Aiming.Amp2, m_head, "Amp2")));
    NamedCommands.registerCommand("autoIntake", new autoIntake(drivetrain, m_intake, m_switch));
    NamedCommands.registerCommand("kindaUpClose", new setHeadTo(Constants.Aiming.kindaUpClose, m_head, "KindaUpClose"));
    NamedCommands.registerCommand("rotateTo", new rotateTo(-50, drivetrain));
    NamedCommands.registerCommand("aimSpeaker", new aimVision(Constants.Aiming.getTag(), drivetrain));
    NamedCommands.registerCommand("midUpClose", new setHeadTo(Constants.Aiming.midUpClose, m_head, "MidUpClose"));
    NamedCommands.registerCommand("Farshot", new ParallelCommandGroup(new setArmTo(Constants.Aiming.farArm, m_arm,"WEEEEE"), new setShooter(Constants.Shooter.shooterSpeed,m_shooter)));
    NamedCommands.registerCommand("FarShotSetup", new setArmTo(Constants.Aiming.farArm, m_arm, "Setuparm"));
    SmartDashboard.putData("Autonomous", m_chooser);

    m_chooser.addOption("4 Note Middle (Stage)", drivetrain.getAutoPath("4note"));
    //  origanl 3 note auto
    m_chooser.addOption("4 Note Middle (AMP)", drivetrain.getAutoPath("Copy of 4note"));
    // four note auto
    m_chooser.addOption("3 Note Middle", drivetrain.getAutoPath("3notemiddle"));
    // simple drive back shoot and pickup shoot
    m_chooser.addOption("3 Note Amp", drivetrain.getAutoPath("3noteamp"));
    // 2 note left attempts 3 notes
    m_chooser.setDefaultOption("3 Note Source", drivetrain.getAutoPath("NewSourceSideAuto"));
    // 2 note right will run out of t\ime trying to shoot the 3rd note
    m_chooser.addOption("Defense", drivetrain.getAutoPath("defense"));
    // funny defense auto
    m_chooser.addOption("4NoteAmp{TEST}", drivetrain.getAutoPath("4noteamp"));
    // test for 4 note
    m_chooser.addOption("Amp Side Around", drivetrain.getAutoPath("AmpSideAround"));
    configureBindings();
    

  }

  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
}