package frc.robot.Commands;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.LimelightHelpers;

public class rotateTo extends Command 
{
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    
    private final CommandSwerveDrivetrain m_drive;

    private double m_target;
    private boolean isfinished;

    public rotateTo(double target, CommandSwerveDrivetrain drive) 
    {
        addRequirements(drive);
        m_drive = drive;
        m_target = target;
    }

    @Override
    public void initialize() 
    {
        isfinished = false;
    }

    @Override
    public void execute() 
    {
        if (m_drive.getState().Pose.getRotation().getDegrees() > (m_target + 2))
        {
            m_drive.setControl(forwardStraight.withRotationalRate(0.4));
            LimelightHelpers.setLEDMode_ForceBlink("limelight");

        }
        else if (m_drive.getState().Pose.getRotation().getDegrees() < (m_target - 2))
        {
            m_drive.setControl(forwardStraight.withRotationalRate(-0.4));
            LimelightHelpers.setLEDMode_ForceBlink("limelight");

        }
        else
        {
            isfinished = true;
            isFinished();
        }
    }

    @Override
    public void end(boolean interrupted) 
    {
        m_drive.setControl(forwardStraight.withRotationalRate(0));
        LimelightHelpers.setLEDMode_ForceOff("limelight");

    }

    @Override
    public boolean isFinished() { return true; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}