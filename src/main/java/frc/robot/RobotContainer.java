package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.autos.*;
import frc.robot.commands.Swerve.TeleopSwerve;
import frc.robot.commands.Swerve.setIntake;
import frc.robot.commands.Swerve.setShooter;
import frc.robot.commands.Swerve.setArm;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    private final XboxController driver = new XboxController(0);
    private final XboxController coDriver = new XboxController(1);

    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kX.value);
   

    /* Codriver Buttons */
    private final JoystickButton intakeButton = new JoystickButton(coDriver, XboxController.Button.kLeftBumper.value);
    private final JoystickButton shooterButton = new JoystickButton(coDriver, XboxController.Button.kRightBumper.value);
    private final JoystickButton reverseIntake = new JoystickButton(coDriver, XboxController.Button.kA.value);
    private final JoystickButton arm = new JoystickButton(coDriver, XboxController.Axis.kLeftY.value);

    /* Subsystems */
    private final Swerve s_Swerve = new Swerve();
    private final Arm m_arm = new Arm();

    /* Auto List */
    SendableChooser<Command> m_chooser = new SendableChooser<>();

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        m_arm.setDefaultCommand(new setArm(coDriver.getLeftY(), m_arm)); // this is how you get the left stick y value and use it
        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve, 
                () -> -driver.getRawAxis(translationAxis), 
                () -> -driver.getRawAxis(strafeAxis), 
                () -> -driver.getRawAxis(rotationAxis), 
                () -> robotCentric.getAsBoolean()
            )
        );

        // Configure the button bindings
        configureButtonBindings();

        // Configure Autonomous
        SmartDashboard.putData("Autonomous", m_chooser);
        m_chooser.setDefaultOption("Example Auto", new exampleAuto(s_Swerve));
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() 
    {
        /* Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroHeading()));
        
        /*Co driver buttons*/ 
        arm.whileTrue(new setArm(1.0, m_arm));
        intakeButton.whileTrue(new setIntake(1.0, s_Swerve));
        shooterButton.whileTrue(new setShooter(0.25, s_Swerve));
        reverseIntake.whileTrue(new setIntake(-Constants.Swerve.reverseIntakeSpeed, s_Swerve));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() { return m_chooser.getSelected(); }
}
