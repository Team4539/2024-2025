package frc.robot.Commands;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.Constants.Shooter;
import frc.robot.LimelightHelpers.LimelightResults;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.limitSwitchSubsystem;

public class aimVision2 extends Command 
{
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    
    private final VisionSubsystem m_vision;
    
    private final CommandSwerveDrivetrain m_drive;

    private final ShooterSubsystem m_shooter;

    private final IntakeSubsystem m_intake;

    private boolean x_centered;
    private boolean y_centered;
    private boolean finished;
    private int m_tag;

    private final double X_CLOSE_THRESHOLD = 6;
    private final double Y_CLOSE_THRESHOLD = 6;

    public aimVision2(int tag, VisionSubsystem subsystem, CommandSwerveDrivetrain drive_subsystem, ShooterSubsystem shooter_subsystem, IntakeSubsystem intake_subsystem) 
    {
        addRequirements(subsystem, drive_subsystem, shooter_subsystem, intake_subsystem);
        m_tag = tag;
        m_vision = subsystem;
        m_drive = drive_subsystem;
        m_shooter = shooter_subsystem;
        m_intake = intake_subsystem;
    }

    @Override
    public void initialize() 
    {
        x_centered = false;
        y_centered = false;
        finished = false;
    }

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
                    double yaw = target.getYaw();
                    m_shooter.setShooter(Constants.Shooter.shooterSpeed);
                    if (yaw > 1)
                    {
                        if (yaw > 0.5)
                        {
                            // correct angle
                            m_drive.setControl(forwardStraight.withRotationalRate(-1));
                        }
                    }
                    else if (yaw < -1)
                    {
                        if (yaw < -0.5)
                        {
                            // correct other way
                            m_drive.setControl(forwardStraight.withRotationalRate(1));
                        }
                    }
                    else
                    {
                        m_drive.setControl(forwardStraight.withRotationalRate(0));
                        m_shooter.setShooter(Constants.Shooter.shooterSpeed);
                        m_intake.setIntake(Constants.Intake.Speed);
                        Timer m_timer = new Timer();
                        m_timer.start();
                        if (m_timer.hasElapsed(1))
                        {
                            m_timer.stop();
                            finished = true;
                            isFinished();
                        }
                        // shoot here?
                    }
                }
            }
        }
        else
        {
            finished = true;
            isFinished();
        }
    }

    @Override
    public void end(boolean interrupted) 
    {
        m_drive.setControl(forwardStraight.withVelocityX(0).withVelocityY(0.0).withRotationalRate(0));
        m_shooter.setShooter(0.0);
        m_intake.setIntake(0.0);
    }

    @Override
    public boolean isFinished() { return finished; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}