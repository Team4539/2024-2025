/*package frc.robot.Commands;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.visionSubsystem;

public class ssCommand extends Command 
{
    private boolean aimed;
    private final visionSubsystem m_vision;
    private final CommandSwerveDrivetrain m_drivetrain;
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    final double CAMERA_HEIGHT_METERS = Units.inchesToMeters(5.5);

    final double TARGET_HEIGHT_METERS = Units.inchesToMeters(2);

    final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(75);

    final double GOAL_RANGE_METERS = Units.feetToMeters(1);
    
    private IntakeSubsystem m_intake = new IntakeSubsystem();

    public ssCommand(visionSubsystem visionSubsystem, CommandSwerveDrivetrain CommandSwerveDrivetrain) 
    {
        addRequirements(visionSubsystem, CommandSwerveDrivetrain);
        m_vision = visionSubsystem;
        m_drivetrain = CommandSwerveDrivetrain;
    }

    @Override
    public void initialize() 
    {
        aimed = false;
    }
    
    @Override
    public void execute() 
    {
        var result = m_vision.note_camera.getLatestResult();

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
                    m_drivetrain.setControl(forwardStraight.withRotationalRate(-0.3));
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
                double areaperc = result.getBestTarget().getArea();

                if (areaperc < 5.5)
                {
                    ParallelRaceGroup group = new ParallelRaceGroup(new check4Note(), new InstantCommand(() -> m_drivetrain.setControl(forwardStraight.withVelocityX(0.3)), m_drivetrain), new setIntake(0.3, m_intake));
                    group.schedule();
                    
                    //m_drivetrain.setControl(forwardStraight.withVelocityX(0.3));
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