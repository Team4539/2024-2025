package frc.robot.Commands;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.LimelightHelpers;

public class aimVision extends Command 
{
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    
    private final CommandSwerveDrivetrain m_drive;
    private int m_tag;

    public aimVision(int tag, CommandSwerveDrivetrain drive_subsystem) 
    {
        addRequirements(drive_subsystem);
        m_tag = tag;
        m_drive = drive_subsystem;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() 
    {
        double tx = LimelightHelpers.getTX("limelight-tag");
        double id = LimelightHelpers.getFiducialID("limelight-tag");

        if (id == m_tag)
        {
            double kP = .015; 
            double targetingAngularVelocity = tx * kP;
            targetingAngularVelocity *= 1.5 * Math.PI;
            targetingAngularVelocity *= -1.0;

            m_drive.setControl(forwardStraight.withRotationalRate(targetingAngularVelocity));
        }
        else
        {
            m_drive.setControl(forwardStraight.withRotationalRate(0.0));
        }
    }

    @Override
    public void end(boolean interrupted) 
    {
        m_drive.setControl(forwardStraight.withRotationalRate(0));
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}