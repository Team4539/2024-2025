package frc.robot;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Commands.aimVision;
import frc.robot.Commands.setArm;
import frc.robot.Commands.setArmTo;
import frc.robot.Commands.setClimber;
// import frc.robot.Commands.setClimber;
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
import frc.robot.subsystems.LedCommSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class RobotContainer 
{
  private double MaxSpeed = TunerConstants.kSpeedAt12VoltsMps; // kSpeedAt12VoltsMps desired top speed
  private double MaxAngularRate = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity

  /* Setting up bindings for necessary control of the swerve drive platform */
  private final CommandXboxController joystick = new CommandXboxController(0); // My joystick
  private final XboxController coDriver = new XboxController(1); // My co-joystick
  private final XboxController Driver = new XboxController(0); //Driver Buttons
  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.04).withRotationalDeadband(MaxAngularRate * 0.08) // Add a 10% deadband
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric
                                                               // driving in open loop
  
  // driver buttons
  private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
  private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
  private final JoystickButton armOverrideButton = new JoystickButton(Driver, XboxController.Button.kStart.value);
  private final JoystickButton climberUp = new JoystickButton(Driver, XboxController.Button.kX.value);
  private final JoystickButton climberDown = new JoystickButton(Driver, XboxController.Button.kB.value);
  private final JoystickButton slowShootButton = new JoystickButton(Driver, XboxController.Button.kRightBumper.value);

  // co driver buttons
  private final JoystickButton intakeButton = new JoystickButton(coDriver, XboxController.Button.kLeftBumper.value);
  private final JoystickButton shooterButton = new JoystickButton(coDriver, XboxController.Button.kRightBumper.value);
  private final JoystickButton reverseIntake = new JoystickButton(coDriver, XboxController.Button.kStart.value);
  private final JoystickButton setAmpButton = new JoystickButton(coDriver, XboxController.Button.kX.value);
  private final JoystickButton setShootButton = new JoystickButton(coDriver, XboxController.Button.kA.value);
  private final JoystickButton SetMiddleButton = new JoystickButton(coDriver, XboxController.Button.kB.value);
  private final JoystickButton setLineButton = new JoystickButton(coDriver, XboxController.Button.kY.value);

  /* Subsystems */
  public final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain; // My drivetrain
  private final ArmSubsystem m_arm = new ArmSubsystem(); // My arm
  private final IntakeSubsystem m_intake = new IntakeSubsystem(); // My intake
  private final ShooterSubsystem m_shooter = new ShooterSubsystem(); // My shooter
  private final ClimberSubsystem m_climber = new ClimberSubsystem();// My Climber
  public final LedCommSubsystem m_led = new LedCommSubsystem(); // My LED
  public final HeadSubsystem m_head = new HeadSubsystem(); // My head
  public final CommandSwerveDrivetrain vision_drivetrain = TunerConstants.DriveTrain; // My vision drive train
  public final VisionSubsystem m_vision = new VisionSubsystem();

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
    setLineButton.whileTrue(new ParallelCommandGroup(new aimVision(Constants.Aiming.getTag(), m_vision, vision_drivetrain), new setArmTo(Constants.Aiming.lineArm , m_arm, "Line Shot"), new setHeadTo(Constants.Aiming.lineHead, m_head, "Line Shot")));
    shooterButton.whileTrue(new setShooter(Constants.Shooter.shooterSpeed, m_shooter));

    setAmpButton.whileTrue(new ParallelCommandGroup(new setArmTo(Constants.Aiming.Amp , m_arm, "Amp"), new setHeadTo(Constants.Aiming.Amp2, m_head, "Amp2")));
    SetMiddleButton.whileTrue(new ParallelCommandGroup(new setArmTo(Constants.Aiming.Home, m_arm, "Home"), new setHeadTo(Constants.Aiming.Home2, m_head, "Home 2")));
    setShootButton.whileTrue(new setHeadTo(Constants.Aiming.Upclose, m_head, "Upclose"));
    armOverrideButton.whileFalse(new InstantCommand(() -> m_climber.setOverride(false)));
    armOverrideButton.whileTrue(new InstantCommand(() -> m_climber.setOverride(true)));
    climberUp.whileTrue(new setClimber(Constants.Climber.climberUp, m_climber));
    climberDown.whileTrue(new setClimber(Constants.Climber.climberDown, m_climber));
    slowShootButton.whileTrue(new InstantCommand(() -> m_shooter.setSlow(true)));
    slowShootButton.whileFalse(new InstantCommand(() -> m_shooter.setSlow(false)));
  }

  public RobotContainer() 
  {
    NamedCommands.registerCommand("intakept1", new setIntake(Constants.Intake.Speed, m_intake).withTimeout(1));
    NamedCommands.registerCommand("intakept2", new ParallelCommandGroup(new setIntake((-Constants.Intake.Speed * 0.25), m_intake).withTimeout(0.2), new setShooter((-Constants.Shooter.shooterSpeed * .1), m_shooter).withTimeout(0.2)));
    NamedCommands.registerCommand("reverseintake", new setIntake(-Constants.Intake.Speed * 0.25, m_intake).withTimeout(0.2));
    NamedCommands.registerCommand("setShootUpClose", new setHeadTo(Constants.Aiming.Upclose, m_head, "Upclose").withTimeout(1));
    NamedCommands.registerCommand("setShootLineHead", new setHeadTo(Constants.Aiming.lineHead, m_head, "Line").withTimeout(2));
    NamedCommands.registerCommand("setShootLineArm", new setHeadTo(Constants.Aiming.lineArm, m_head, "Line").withTimeout(2));
    NamedCommands.registerCommand("setShooterMotor", new setShooter(Constants.Shooter.shooterSpeed, m_shooter).withTimeout(1));
    NamedCommands.registerCommand("setIntake", new setIntake(Constants.Intake.Speed, m_intake).withTimeout(1));
    NamedCommands.registerCommand("setHome", new ParallelCommandGroup(new setArmTo(Constants.Aiming.Home, m_arm, "Home").withTimeout(1), new setHeadTo(Constants.Aiming.Home2, m_head, "Home 2").withTimeout(1)));
    NamedCommands.registerCommand("autoAim", new aimVision(Constants.Aiming.getTag(), m_vision, vision_drivetrain).withTimeout(2));
    SmartDashboard.putData("Autonomous", m_chooser);
    m_chooser.setDefaultOption("Just Drive", drivetrain.getAutoPath("JustDrive"));
    m_chooser.addOption("Middle Shoot Drive Back Pickup", drivetrain.getAutoPath("ShootDriveBackPickup"));
    m_chooser.addOption("3 Note Center Blue", drivetrain.getAutoPath("3 Note South Blue"));
    m_chooser.addOption("3 Note Amp Blue", drivetrain.getAutoPath("3 Note North Blue"));
    m_chooser.addOption("3 Note Center", drivetrain.getAutoPath("3 Note Center Red"));
    m_chooser.addOption("3 Note Amp Red", drivetrain.getAutoPath("3 Note North Red"));
    m_chooser.addOption("2 Note Center ", drivetrain.getAutoPath("2 Note Center"));
    m_chooser.addOption("1 Note South Send", drivetrain.getAutoPath("1 Note South Send"));
    m_chooser.addOption("Test Test", drivetrain.getAutoPath("Test test"));
    m_chooser.addOption("(v2) 2 Note Center", drivetrain.getAutoPath("2 Note Center Red"));
    m_chooser.addOption("1 note", drivetrain.getAutoPath("1 Note Stay"));
    m_chooser.addOption("Defense", drivetrain.getAutoPath("Defense"));
    m_chooser.addOption("Test", drivetrain.getAutoPath("Test"));

    configureBindings();
  }

  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
}