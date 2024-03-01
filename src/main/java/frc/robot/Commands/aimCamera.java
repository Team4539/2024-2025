package frc.robot.Commands;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.visionSubsystem;

public class aimCamera extends Command 
{
    private boolean aimed;
    private final visionSubsystem m_vision;
    private final CommandSwerveDrivetrain m_drivetrain;
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    private final double CAMERA_HEIGHT_METERS = Units.inchesToMeters(13.5);

    private double TARGET_HEIGHT_METERS; // Units.inchesToMeters(2);

    private final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(63.5);

    private final double GOAL_RANGE_METERS;
    
    private final int m_tag;

    private IntakeSubsystem m_intake = new IntakeSubsystem();

    public aimCamera(int april_tag, double goal_range, visionSubsystem visionSubsystem, CommandSwerveDrivetrain CommandSwerveDrivetrain) 
    {
        addRequirements(visionSubsystem, CommandSwerveDrivetrain);
        m_vision = visionSubsystem;
        m_drivetrain = CommandSwerveDrivetrain;
        GOAL_RANGE_METERS = Units.inchesToMeters(goal_range);
        m_tag = april_tag;
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
        var result = m_vision.note_camera.getLatestResult();
        if (result.getBestTarget().getFiducialId() == m_tag)
        {
            if (!aimed)
            {
                if (result.hasTargets()) 
                {
                    double rotationYaw = result.getBestTarget().getYaw();
                
                    if ((rotationYaw - 2) > 0)
                    {
                        m_drivetrain.setControl(forwardStraight.withRotationalRate(0.3));
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
                    if (range > GOAL_RANGE_METERS)
                    {
                        m_drivetrain.setControl(forwardStraight.withVelocityX(0.3));
                    }
                    else
                    {
                        // end
                        m_drivetrain.setControl(forwardStraight.withVelocityX(0));
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