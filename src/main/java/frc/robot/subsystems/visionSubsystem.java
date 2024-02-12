package frc.robot.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

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
            SmartDashboard.putNumber("[ID " + target.getFiducialId() + "] DTT", target.getBestCameraToTarget().getTranslation().getY()); // should be z but that doesn't work :()
        }
    }
}
