package frc.robot;

import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.pathplanner.lib.auto.NamedCommands;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Commands.aimCamera;
import frc.robot.Commands.setArm;
import frc.robot.Commands.setArmTo;
import frc.robot.Commands.setIntake;
import frc.robot.Commands.setShooter;
import frc.robot.Commands.setClimber;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LedCommSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.visionSubsystem;

public class RobotContainer 
{
  private double MaxSpeed = TunerConstants.kSpeedAt12VoltsMps; // kSpeedAt12VoltsMps desired top speed
  private double MaxAngularRate = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity

  /* Setting up bindings for necessary control of the swerve drive platform */
  private final CommandXboxController joystick = new CommandXboxController(0); // My joystick
  private final XboxController coDriver = new XboxController(1); // My co-joystick
  private final XboxController Driver = new XboxController(0); //Driver Buttons
  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric
                                                               // driving in open loop
  
  // driver buttons
  private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
  private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    //private final JoystickButton climberButton = new JoystickButton(Driver, XboxController.Button.kRightBumper.value);
    //private final JoystickButton reverseClimberButton = new JoystickButton(Driver, XboxController.Button.kLeftBumper.value);

  // co driver buttons
  private final JoystickButton intakeButton = new JoystickButton(coDriver, XboxController.Button.kLeftBumper.value);
  private final JoystickButton shooterButton = new JoystickButton(coDriver, XboxController.Button.kRightBumper.value);
  private final JoystickButton reverseIntake = new JoystickButton(coDriver, XboxController.Button.kY.value);
  private final JoystickButton setSourceButton = new JoystickButton(coDriver, XboxController.Button.kA.value);
  private final JoystickButton setAmpButton = new JoystickButton(coDriver, XboxController.Button.kB.value);
  private final JoystickButton setHomeButton = new JoystickButton(coDriver, XboxController.Button.kStart.value);
  private final JoystickButton setShootButton = new JoystickButton(coDriver, XboxController.Button.kBack.value);
  //private final JoystickButton aimButton = new JoystickButton(coDriver, XboxController.Button.kX.value);

  /* Subsystems */
  public final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain; // My drivetrain
  private final ArmSubsystem m_arm = new ArmSubsystem(); // My arm
  private final IntakeSubsystem m_intake = new IntakeSubsystem(); // My intake
  private final ShooterSubsystem m_shooter = new ShooterSubsystem(); // My shooter
  private final visionSubsystem m_vision = new visionSubsystem(); // My vision
  private final ClimberSubsystem m_climber = new ClimberSubsystem();// My Climber
  public final LedCommSubsystem m_led = new LedCommSubsystem(); // My LED

  /* Auto List */
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  private void configureBindings() 
  {
    m_vision.periodic(); // start vision
    m_arm.setDefaultCommand(
            new setArm(() -> coDriver.getRawAxis(XboxController.Axis.kLeftY.value), m_arm)); // this is how you get the left stick y value and use it
    m_climber.setDefaultCommand(
            new setClimber(() -> coDriver.getRawAxis(XboxController.Axis.kRightY.value), m_climber));
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
    reverseIntake.whileTrue(new setIntake((-Constants.Intake.Speed + 0.25), m_intake));
    shooterButton.whileTrue(new setShooter(Constants.Shooter.shooterSpeed, m_shooter));
    //aimButton.whileTrue(new aimCamera(0, 2, m_vision, drivetrain));
    setSourceButton.whileTrue(new setArmTo(Constants.Aiming.Source, m_arm, "Source"));
    setAmpButton.whileTrue(new setArmTo(Constants.Aiming.Amp , m_arm, "Amp"));
    setHomeButton.whileTrue(new setArmTo(Constants.Aiming.Home, m_arm, "home"));
    setShootButton.whileTrue(new setArmTo(Constants.Aiming.Position, m_arm, "Position"));
    //climberButton.whileTrue(new setClimber(Constants.Climber.Speed, m_climber));
    //reverseClimberButton.whileTrue(new setClimber((-Constants.Climber.Speed), m_climber));
  }

  public RobotContainer() 
  {
    NamedCommands.registerCommand("intake", new setIntake(Constants.Intake.Speed, m_intake).withTimeout(1));
    NamedCommands.registerCommand("shoot", new setShooter(Constants.Shooter.shooterSpeed, m_shooter).withTimeout(1));
    NamedCommands.registerCommand("reverseintake", new setIntake(-Constants.Intake.Speed * 0.25, m_intake).withTimeout(0.2));
    NamedCommands.registerCommand("setShoot", new setArmTo(Constants.Aiming.Position, m_arm, "Position") );
    NamedCommands.registerCommand("Home", new setArmTo(Constants.Aiming.Home, m_arm, "home").withTimeout(2));
    configureBindings();
    SmartDashboard.putData("Autonomous", m_chooser);
    m_chooser.setDefaultOption("shoot, intake, shoot", drivetrain.getAutoPath("sis 1"));
    m_chooser.setDefaultOption("test", drivetrain.getAutoPath("test"));
    m_chooser.addOption("3 note", drivetrain.getAutoPath("3 Note"));
    m_chooser.addOption("SR N8", drivetrain.getAutoPath("Sr N8"));
    //m_chooser.setDefaultOption("10 feet", drivetrain.getAutoPath("10"));
   // m_chooser.setDefaultOption("rotate", drivetrain.getAutoPath("rotate"));
  }

  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
}
