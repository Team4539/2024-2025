
/* Removed Temporarily */

/*package frc.robot.Commands;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.subsystems.ArmPositionCalculator;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.visionSubsystem;

public class aimCamera extends Command 
{
    private boolean aimed;
    private final visionSubsystem m_vision;
    private final CommandSwerveDrivetrain m_drivetrain;
    private final ArmSubsystem m_arms;
    private final PIDController pidController;
    private double fixedOutput;

    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    private final double CAMERA_HEIGHT_METERS = Units.inchesToMeters(13.5);

    private double TARGET_HEIGHT_METERS; // Units.inchesToMeters(2);

    private final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(24);
    
    private final int m_tag;

    public aimCamera(int april_tag, visionSubsystem visionSubsystem, CommandSwerveDrivetrain CommandSwerveDrivetrain, ArmSubsystem arm) 
    {
        addRequirements(visionSubsystem, CommandSwerveDrivetrain, arm);
        m_vision = visionSubsystem;
        m_drivetrain = CommandSwerveDrivetrain;
        m_tag = april_tag;
        m_arms = arm;
        pidController = new PIDController(0.175, 0.002, 0.0); // Adjust these values as needed
    }

    @Override
    public void initialize() 
    {
        aimed = false;

        // Values from here: https://i.imgur.com/2jxXO19.png
        switch (m_tag)
        {
            case 1:
            case 2:
            case 5:
            case 6:
            case 9:
            case 10:
                TARGET_HEIGHT_METERS = 1.36;
                break;
            case 3:
            case 4:
            case 7:
            case 8:
                TARGET_HEIGHT_METERS = 1.45;
                break;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                TARGET_HEIGHT_METERS = 1.32;
                break;
            default:
                TARGET_HEIGHT_METERS = -1;
                break;
        }
    }
    
    @Override
    public void execute() 
    {
        var result = m_vision.april_tag_camera.getLatestResult();
        if (result.hasTargets())
        {
            if (result.getBestTarget().getFiducialId() == m_tag)
            {
                if (!aimed)
                {
                    if (result.hasTargets()) 
                    {
                        double rotationYaw = result.getBestTarget().getYaw();
                
                        if ((rotationYaw - 2) > 0)
                        {
                            m_drivetrain.setControl(forwardStraight.withRotationalRate(-0.3));
                        }
                        else if ((rotationYaw + 2) < 0)
                        {
                            m_drivetrain.setControl(forwardStraight.withRotationalRate(0.3));
                        }
                        else
                        {
                            m_drivetrain.setControl(forwardStraight.withRotationalRate(0));
                            aimed = true;
                        }
                    }
                }
                else if (aimed)
                {
                    if (result.hasTargets())
                    {
                        // returns the distance to target in meters
                        double range = PhotonUtils.calculateDistanceToTargetMeters(
                            CAMERA_HEIGHT_METERS,
                            TARGET_HEIGHT_METERS,
                            CAMERA_PITCH_RADIANS,
                            Units.degreesToRadians(result.getBestTarget().getPitch()));

                        double goal_arm_rot = ArmPositionCalculator.calculateArmPosition((int)Units.metersToInches(range));
                        SmartDashboard.putNumber("Calculated Distance to Target (inch)", (int)Units.metersToInches(range));
                        SmartDashboard.putNumber("Calculated Arm Position", goal_arm_rot);
                        double encoder = m_arms.getEncoder();
                        double output = pidController.calculate(encoder, goal_arm_rot);
                        if (output > 1)
                        {
                            fixedOutput = 1; 
                        }
                        else 
                        {
                            fixedOutput = output;
                        }

                        if (encoder > goal_arm_rot) // go down
                        {
                            m_arms.setArm(-fixedOutput*.65); 
                        }
                        else if (encoder < goal_arm_rot) //me go up
                        {
                            m_arms.setArm(-fixedOutput); 

                        }
                        else //stay
                        {
                            m_arms.setArm(0);
                        }
                        SmartDashboard.putNumber("output", output);
                        SmartDashboard.putString("Command", "aimCamera.java");
                    }
                }
            }
        }
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; } 
}
*/