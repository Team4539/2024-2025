package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

public class VisionSubsystem extends SubsystemBase
{
    public PhotonCamera camera;

    public VisionSubsystem()
    {
        camera = new PhotonCamera("OV9281");
    }

    @Override
    public void periodic() 
    {
        SmartDashboard.putBoolean("Vision Has Target(s)", hasResults());
    }

    public boolean hasResults()
    {
        return camera.getLatestResult().hasTargets();
    }
}