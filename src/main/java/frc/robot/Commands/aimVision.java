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
import frc.robot.Constants.Shooter;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LimelightHelpers;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.limitSwitchSubsystem;
import frc.robot.subsystems.LimelightHelpers.LimelightResults;

public class aimVision extends Command 
{
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    
    private final CommandSwerveDrivetrain m_drive;

    private final ShooterSubsystem m_shooter;

    private final IntakeSubsystem m_intake;

    private boolean x_centered;
    private boolean y_centered;
    private boolean finished;
    private int m_tag;

    private final double X_CLOSE_THRESHOLD = 6;
    private final double Y_CLOSE_THRESHOLD = 6;

    public aimVision(int tag, CommandSwerveDrivetrain drive_subsystem, ShooterSubsystem shooter_subsystem, IntakeSubsystem intake_subsystem) 
    {
        addRequirements(drive_subsystem, shooter_subsystem, intake_subsystem);
        m_tag = tag;
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
        // get tx, ty, and id from network tables
        // returns 0.0 if no target is found
        double tx = LimelightHelpers.getTX("limelight-main");
        double ty = LimelightHelpers.getTY("limelight-main");
        double ta = LimelightHelpers.getTA("limelight-main");
        double id = LimelightHelpers.getFiducialID("limelight-main");

        // if id matches target
        if (id == m_tag)
        {
            // if not centered / no target is found
            if (Math.abs(tx) > 0.1 || Math.abs(ty) > 0.1 || ta < 0.1 || ta > 0.2)
            {
                if (tx > 0.1) 
                {
                    // Move the robot to the right
                    m_drive.setControl(forwardStraight.withVelocityY(-0.4));
                } 
                else if (tx < -0.1) 
                {
                    // Move the robot to the left
                    m_drive.setControl(forwardStraight.withVelocityY(0.4));
                }
                if (ty > 0.1) 
                {
                    // Move the robot up
                    m_drive.setControl(forwardStraight.withVelocityX(0.4));
                } 
                else if (ty < -0.1) 
                {
                    // Move the robot down
                    m_drive.setControl(forwardStraight.withVelocityX(-0.4));
                }

                if (ta < 0.1) 
                {
                    // Move the robot forward
                    m_drive.setControl(forwardStraight.withRotationalRate(0.4));
                } 
                else if (ta > 0.2) 
                {
                    // Move the robot backward
                    m_drive.setControl(forwardStraight.withRotationalRate(-0.4))
                }
            }
            else
            {
                // robot is centered
                finished = true;
                isFinished();
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