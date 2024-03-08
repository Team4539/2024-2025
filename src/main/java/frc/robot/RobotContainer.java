package frc.robot;

// import org.photonvision.PhotonUtils;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
// import frc.robot.Commands.aimCamera;
import frc.robot.Commands.setArm;
import frc.robot.Commands.setArmTo;
import frc.robot.Commands.setClimber;
import frc.robot.Commands.setIntake;
import frc.robot.Commands.setShooter;
// import frc.robot.Commands.ssCommand;
import frc.robot.generated.TunerConstants;
// import frc.robot.subsystems.ArmPositionCalculator;
// import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LedCommSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
// import frc.robot.subsystems.visionSubsystem;

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

  // co driver buttons
  private final JoystickButton intakeButton = new JoystickButton(coDriver, XboxController.Button.kLeftBumper.value);
  private final JoystickButton shooterButton = new JoystickButton(coDriver, XboxController.Button.kRightBumper.value);
  private final JoystickButton reverseIntake = new JoystickButton(coDriver, XboxController.Button.kY.value);
  private final JoystickButton setSourceButton = new JoystickButton(coDriver, XboxController.Button.kA.value);
  private final JoystickButton setAmpButton = new JoystickButton(coDriver, XboxController.Button.kB.value);
  private final JoystickButton halfpowerShootButton = new JoystickButton(coDriver, XboxController.Button.kX.value);
  private final JoystickButton setShootButton = new JoystickButton(coDriver, XboxController.Button.kBack.value);
  private final JoystickButton SetMiddleButton = new JoystickButton(coDriver, XboxController.Button.kStart.value);
  //private final JoystickButton aimButton = new JoystickButton(Driver, XboxController.Button.kB.value);

  /* Subsystems */
  public final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain; // My drivetrain
  private final ArmSubsystem m_arm = new ArmSubsystem(); // My arm
  private final IntakeSubsystem m_intake = new IntakeSubsystem(); // My intake
  private final ShooterSubsystem m_shooter = new ShooterSubsystem(); // My shooter
  // private final visionSubsystem m_vision = new visionSubsystem(); // My vision
  private final ClimberSubsystem m_climber = new ClimberSubsystem();// My Climber
  public final LedCommSubsystem m_led = new LedCommSubsystem(); // My LED

  /* Auto List */
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  private void configureBindings() 
  {
    // m_vision.periodic(); // start vision
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
    reverseIntake.whileTrue(new setIntake((-Constants.Intake.Speed * 0.25), m_intake));
    reverseIntake.whileTrue(new setShooter((-Constants.Shooter.shooterSpeed * .1),m_shooter));
    shooterButton.whileTrue(new setShooter(Constants.Shooter.shooterSpeed, m_shooter));
    //aimButton.whileTrue(new aimCamera(0, 2, m_vision, drivetrain));
    setSourceButton.whileTrue(new setArmTo(Constants.Aiming.Source, m_arm, "Source"));
    setAmpButton.whileTrue(new setArmTo(Constants.Aiming.Amp , m_arm, "Amp"));
    SetMiddleButton.whileTrue(new setArmTo(Constants.Aiming.Home, m_arm, "Home"));
    //setHomeButton.whileTrue(new setArmTo(Constants.Aiming.Home, m_arm, "home"));
    setShootButton.whileTrue(new setArmTo(Constants.Aiming.Position, m_arm, "Position"));
    halfpowerShootButton.whileTrue(new setShooter(Constants.Shooter.shooterSpeed * .85, m_shooter));
    //aimButton.whileTrue(new aimCamera(Constants.Aiming.getTag(), m_vision, drivetrain, m_arm));
    
    armOverrideButton.whileFalse(new InstantCommand(() -> m_climber.setOverride(false)));
    armOverrideButton.whileTrue(new InstantCommand(() -> m_climber.setOverride(true)));
  }

  public RobotContainer() 
  {
    NamedCommands.registerCommand("intake", new setIntake(Constants.Intake.Speed, m_intake).withTimeout(1));
    NamedCommands.registerCommand("shoot", new setShooter(Constants.Shooter.shooterSpeed, m_shooter).withTimeout(.75));
    NamedCommands.registerCommand("reverseintake", new setIntake(-Constants.Intake.Speed * 0.25, m_intake).withTimeout(0.2));
    NamedCommands.registerCommand("setShoot", new setArmTo(Constants.Aiming.Position, m_arm, "Position").withTimeout(5) );
    NamedCommands.registerCommand("Home", new setArmTo(Constants.Aiming.Home, m_arm, "home").withTimeout(2));
    configureBindings();
    SmartDashboard.putData("Autonomous", m_chooser);
    // SmartDashboard.putData("Search for Note", new ssCommand(m_vision, drivetrain));
    //SmartDashboard.putData("Search for April Tag 7", new aimCamera(7, 86, m_vision, drivetrain));
    //m_chooser.setDefaultOption("(Center) Shoot, Drive Back and Intake", drivetrain.getAutoPath("!csdin"));
    //m_chooser.addOption("(Left) Shoot, Drive Back and Intake", drivetrain.getAutoPath("!lsdin"));
    //m_chooser.addOption("(Right) Shoot, Drive Back and Intake", drivetrain.getAutoPath("!rsdin"));
    m_chooser.addOption("3 Note Center Blue", drivetrain.getAutoPath("3 Note South Blue"));
    m_chooser.addOption("3 Note Amp Blue", drivetrain.getAutoPath("3 Note North Blue"));
    //m_chooser.addOption("3 Note Center", drivetrain.getAutoPath("3 Note Center"));
    m_chooser.addOption("3 Note Center", drivetrain.getAutoPath("3 Note Center Red"));
    m_chooser.addOption("3 Note Amp Red", drivetrain.getAutoPath("3 Note North Red"));
    m_chooser.addOption("2 Note Center ", drivetrain.getAutoPath("2 Note Center"));
    m_chooser.setDefaultOption("1 note", drivetrain.getAutoPath("1 Note Stay"));
    m_chooser.addOption("1 Note South Send", drivetrain.getAutoPath("1 Note South Send"));
    m_chooser.addOption("Test Test", drivetrain.getAutoPath("Test test"));
    m_chooser.addOption("(v2) 2 Note Center", drivetrain.getAutoPath("2 Note Center Red"));
    //m_chooser.addOption("Test", drivetrain.getAutoPath("Test"));
  }

  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
}
