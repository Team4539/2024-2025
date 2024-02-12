package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberSubsystem extends SubsystemBase
{
    public TalonFX climber;

    public ClimberSubsystem()
    {
        climber = new TalonFX(Constants.Climber.ID);
        climber.setInverted(true);
        climber.setNeutralMode(NeutralModeValue.Brake);
    }

    public void setClimber(double speed)
    {
        climber.set(speed);
    }
}
