package frc.robot.Commands;

import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.visionSubsystem;

public class aimCamera extends Command 
{
    private final visionSubsystem m_vision;
    private final CommandSwerveDrivetrain m_drivetrain;
    private int m_targetID;
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    public aimCamera(int targetID, visionSubsystem visionSubsystem, CommandSwerveDrivetrain CommandSwerveDrivetrain) 
    {
        addRequirements(visionSubsystem, CommandSwerveDrivetrain);
        m_vision = visionSubsystem;
        m_drivetrain = CommandSwerveDrivetrain;
        m_targetID = targetID;
        
    }

    @Override
    public void initialize() 
    {
        if (m_targetID == 0) // not assigned
        {
            if (DriverStation.getAlliance().get() == Alliance.Blue)
            {
                m_targetID = Constants.blueTarget;
            }
            else
            {
                m_targetID = Constants.redTarget;
            }
        }
    }
    
    @Override
    public void execute() 
    {
        PhotonPipelineResult result = m_vision.camera.getLatestResult();
        PhotonTrackedTarget m_target = null;
        
        for (var target : result.getTargets())
        {
            if (target.getFiducialId() == m_targetID)
            {
                m_target = target; // if we found what we are looking for
            }
        }

        if (m_target == null)
        {
            m_target = result.getBestTarget(); // if we didn't we just use best target
        }

        double x = m_target.getBestCameraToTarget().getTranslation().getY(); // it should be x but that doesn't work :( // give aiden credit
        double distance = m_target.getBestCameraToTarget().getTranslation().getX(); // TODO: test this for distance

        if (m_target != null) 
        {
            SmartDashboard.putNumber("ID: " + m_target.getFiducialId() + " Distance" , distance);
        
            if (x > 0) 
            {
                // Target is to the right of the center, move camera right
                // turn robot to the right
                m_drivetrain.setControl(forwardStraight.withRotationalRate(0.5));
            } 
            else if (x < 0) 
            {
                // Target is to the left of the center, move camera left
                // turn robot to the left
                m_drivetrain.setControl(forwardStraight.withRotationalRate(-0.5));
            }
            else
            {
                // Target is centered
                m_drivetrain.setControl(forwardStraight.withRotationalRate(0));
            }
        }
            
    }
    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}