package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase
{
    public TalonFX shooter;
    public TalonFX shooterInverted;
    public boolean override = false;
    public boolean slow = false;
    public ShooterSubsystem()
    {
   
        shooter = new TalonFX(Constants.Shooter.ID);
        shooterInverted = new TalonFX(Constants.Shooter.InvertedID);
        
        shooter.setNeutralMode(NeutralModeValue.Brake);
        shooterInverted.setNeutralMode(NeutralModeValue.Brake);
    }

    public void setSlow(boolean value)
    {
        slow = value;
    }

    public void setShooter(double speed)
    {
        if (slow)
        {
            shooter.set(speed * 0.2);
            shooterInverted.set(speed * 0.2);
        }
        else
        {
            shooter.set(speed);
            shooterInverted.set(speed);
        }
    }
}
