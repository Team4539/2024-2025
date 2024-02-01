package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase
{
    public TalonFX shooter;
    public TalonFX shooterInverted;
    public boolean override = false;

    public ShooterSubsystem()
    {
        shooter = new TalonFX(Constants.Shooter.ID);
        shooterInverted = new TalonFX(Constants.Shooter.InvertedID);
        shooterInverted.setInverted(true);
    }

    public void setShooter(double speed)
    {
        shooter.set(speed);
        shooterInverted.set(speed);
    }
}
