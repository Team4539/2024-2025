package frc.robot.Commands;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
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

    private final double X_CLOSE_THRESHOLD = 6;
    private final double Y_CLOSE_THRESHOLD = 6;

    private final PIDController pidController;

    public autoIntake(VisionSubsystem subsystem, CommandSwerveDrivetrain drive_subsystem, IntakeSubsystem intake) 
    {
        addRequirements(subsystem, drive_subsystem, intake);
        m_vision = subsystem;
        m_drive = drive_subsystem;
        m_intake = intake;
        pidController = new PIDController(10, 0.0, 0); // TODO: tune this
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
            double xCorrection = calculateCorrection(tx);
            double yCorrection = calculateCorrection(ty);
        
            applyCorrection(tx, ty, xCorrection, yCorrection);
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

    private double calculateCorrection(double offset) {
        if (Math.abs(offset) > 0.5) {
            return offset > 0 ? -0.65 : 0.65;
        } else {
            return 0;
        }
    }
    
    private void applyCorrection(double tx, double ty, double xCorrection, double yCorrection) 
    {
        if (Math.abs(tx) > 0.05) 
        {
            double output = pidController.calculate(xCorrection, tx);
            if (output < 1){ output = 1; }
            SmartDashboard.putNumber("PID", output);
            m_drive.setControl(forwardStraight.withRotationalRate(xCorrection));
        } 
        else 
        {
            x_centered = true;
        }
    
        if (Math.abs(ty) > 0.05) 
        {
            m_drive.setControl(forwardStraight.withVelocityX(yCorrection > 0 ? 2 : 0));
        } else 
        {
            y_centered = true;
        }
    
        if (Math.abs(tx) < X_CLOSE_THRESHOLD || Math.abs(ty) < Y_CLOSE_THRESHOLD) 
        {
            m_intake.setIntake(Constants.Intake.Speed);
        }
    
        if (x_centered && y_centered) {
            m_drive.setControl(forwardStraight.withVelocityX(0.8));
        } else {
            m_drive.setControl(forwardStraight.withVelocityY(0).withVelocityX(0));
            m_drive.setControl(forwardStraight.withVelocityX(0.8));
        }
    }
}