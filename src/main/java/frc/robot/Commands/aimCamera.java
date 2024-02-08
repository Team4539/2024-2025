package frc.robot.Commands;

import java.util.function.DoubleSupplier;

import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.visionSubsystem;

public class aimCamera extends Command 
{
    private final visionSubsystem m_vision;
    private final CommandSwerveDrivetrain m_drivetrain;
    private int m_targetIDRed;
    private int m_targetIDBlue;
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    public double x;

    public aimCamera(int targetIDRed, int targetIDBlue, visionSubsystem visionSubsystem, CommandSwerveDrivetrain CommandSwerveDrivetrain) 
    {
        addRequirements(visionSubsystem, CommandSwerveDrivetrain);
        m_vision = visionSubsystem;
        m_drivetrain = CommandSwerveDrivetrain;
        m_targetIDRed = targetIDRed;
        m_targetIDBlue = targetIDBlue;
    }

    @Override
    public void initialize() {}
    
    @Override
    public void execute() 
    {
        PhotonPipelineResult result = m_vision.camera.getLatestResult();
        PhotonTrackedTarget m_target = null;
    
        for (var target : result.getTargets())
        {
            if (target.getFiducialId() == m_targetIDRed)
            {
                m_target = target; // if we found what we are looking for
                x = m_target.getBestCameraToTarget().getTranslation().getY(); // MY X
            }
            else if (target.getFiducialId() == m_targetIDBlue)
            {
                 m_target = target; // if we found what we are looking for
                x = m_target.getBestCameraToTarget().getTranslation().getY(); // MY X
            }
            else
            {
                m_target = null;
                x = 0;
            }
        }

        if (m_target == null)
        {
            m_target = result.getBestTarget(); // if we didn't we just use best target
        }

        
        SmartDashboard.putNumber("MY X", x);
    
    
        if (x > 0.05
        ) 
        {
            // Target is to the right of the center, move camera right
            // turn robot to the right
            m_drivetrain.setControl(forwardStraight.withVelocityX(0).withVelocityY(-0.5));
          
        } 
        else if (x < -0.05) 
        {
            // Target is to the left of the center, move camera left
            // turn robot to the left
            m_drivetrain.setControl(forwardStraight.withVelocityX(0).withVelocityY(0.5));
           

        }
        else
        {
            // Target is centered
            m_drivetrain.setControl(forwardStraight.withVelocityX(0).withVelocityY(0.0));
          

        }
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}