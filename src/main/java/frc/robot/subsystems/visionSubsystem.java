package frc.robot.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class visionSubsystem extends SubsystemBase
{
    public PhotonCamera april_tag_camera;
    public PhotonCamera note_camera;

    public visionSubsystem()
    {
        april_tag_camera = new PhotonCamera("Global_Shutter_Camera");
        note_camera = new PhotonCamera("Microsoft_LifeCam_HD-3000");
    }

    @Override
    public void periodic() 
    {
        SmartDashboard.putBoolean("April Tag Detection", isResults(april_tag_camera));
        SmartDashboard.putBoolean("Note Detection", isResults(note_camera));

        if (note_camera.getLatestResult().hasTargets())
        {
            SmartDashboard.putNumber("Note Yaw", note_camera.getLatestResult().getBestTarget().getYaw());
        }
    }

    public boolean isResults(PhotonCamera camera)
    {
        return camera.getLatestResult().hasTargets();
    }

    public void distanceToTarget(PhotonCamera camera)
    {
        PhotonPipelineResult result = camera.getLatestResult();
        for (var target : result.getTargets())
        {
            SmartDashboard.putNumber("[ID " + target.getFiducialId() + "] DTT", target.getBestCameraToTarget().getTranslation().getY()); // should be z but that doesn't work :()
        }
    }
}
