package frc.robot.Commands;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class autoIntake extends Command 
{
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    
    private final VisionSubsystem m_vision;
    
    private final CommandSwerveDrivetrain m_drive;

    private final IntakeSubsystem m_intake;

    private boolean x_centered;
    private boolean y_centered;

    public autoIntake(VisionSubsystem subsystem, CommandSwerveDrivetrain drive_subsystem, IntakeSubsystem intake) 
    {
        addRequirements(subsystem, drive_subsystem, intake);
        m_vision = subsystem;
        m_drive = drive_subsystem;
        m_intake = intake;
    }

    @Override
    public void initialize() 
    {
        x_centered = false;
        y_centered = false;
    }

    @Override
    public void execute() 
    {
        // left is down x right is up x towards robot is down y away from robot is up y
        double tx = LimelightHelpers.getTX("limelight-main");
        double ty = LimelightHelpers.getTY("limelight-main");
        if (tx != 0.0 && ty != 0.0)
        {
            if (!x_centered)
            {
                if (tx < -5)
                {
                    m_drive.setControl(forwardStraight.withVelocityY(0.8));
                }
                else if (tx > 0)
                {
                    m_drive.setControl(forwardStraight.withVelocityY(-0.8));
                }
                else
                {
                    m_drive.setControl(forwardStraight.withVelocityY(0));
                    x_centered = true;
                }
            }
            else if (!y_centered)
            {
                if (ty > 5)
                {
                    m_drive.setControl(forwardStraight.withVelocityX(0.8));
                }
                else
                {
                    m_drive.setControl(forwardStraight.withVelocityX(0));
                    y_centered = true;
                }
            }
            else
            {
                m_drive.setControl(forwardStraight.withVelocityX(0.8));
                m_intake.setIntake(Constants.Intake.Speed);
            }
        }
        else
        {
            m_drive.setControl(forwardStraight.withVelocityY(0).withVelocityX(0));
            m_drive.setControl(forwardStraight.withVelocityX(0.8));
            m_intake.setIntake(Constants.Intake.Speed);
        }
    }

    @Override
    public void end(boolean interrupted) 
    {
        m_drive.setControl(forwardStraight.withVelocityY(0.0));
        m_intake.setIntake(0);
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}