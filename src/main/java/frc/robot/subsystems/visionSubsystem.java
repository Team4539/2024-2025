package frc.robot.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class visionSubsystem extends SubsystemBase
{
    public PhotonCamera camera;

    public visionSubsystem()
    {
        camera = new PhotonCamera("Microsoft_LifeCam_HD-3000");
    }

    @Override
    public void periodic() 
    {
        SmartDashboard.putBoolean("Detection", isResults());

        if (isResults())
        {
            distanceToTarget();
        }
    }

    public boolean isResults()
    {
        return camera.getLatestResult().hasTargets();
    }

    public void distanceToTarget()
    {
        PhotonPipelineResult result = camera.getLatestResult();
        for (var target : result.getTargets())
        {
            SmartDashboard.putNumber("[ID " + target.getFiducialId() + "] DTT", target.getBestCameraToTarget().getTranslation().getX()); // should be z but that doesn't work :()
        }
    }

    public int aimOnTarget(int targetID)
    {
        PhotonPipelineResult result = camera.getLatestResult();
        PhotonTrackedTarget m_target;
        
        for (var target : result.getTargets())
        {

        }

        double x = target.getBestCameraToTarget().getTranslation().getZ(); // it should be x but that doesn't work :(        
        if (x > 0) 
        {
            // Target is to the right of the center, move camera right
            // turn robot to the right
        } 
        else if (x < 0) 
        {
            // Target is to the left of the center, move camera left
            // turn robot to the left
        }
        else
        {
            // Target is centered
        }
    }
}
