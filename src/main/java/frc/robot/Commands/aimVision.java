package frc.robot.Commands;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.VisionSubsystem;

public class aimVision extends Command 
{
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    
    private final VisionSubsystem m_vision;
    
    private final CommandSwerveDrivetrain m_drive;

    private int m_tag;

    public aimVision(int target, VisionSubsystem subsystem, CommandSwerveDrivetrain drive_subsystem) 
    {
        addRequirements(subsystem, drive_subsystem);
        m_vision = subsystem;
        m_drive = drive_subsystem;
        m_tag = target;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() 
    {
        var result = m_vision.camera.getLatestResult();
        if (result.hasTargets())
        {
            for (var target: result.getTargets())
            {
                if (target.getFiducialId() == m_tag)
                {
                    double rotationYaw = target.getYaw();
                    if ((rotationYaw - 2) > 0)
                    {
                        m_drive.setControl(forwardStraight.withRotationalRate(-0.6));
                    }
                    else if ((rotationYaw + 2) < 0)
                    {
                        m_drive.setControl(forwardStraight.withRotationalRate(0.6));
                    }
                    else
                    {
                        m_drive.setControl(forwardStraight.withRotationalRate(0));
                    }
                }
            }
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