package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase
{
    public TalonFX intake;

    public IntakeSubsystem()
    {
        intake = new TalonFX(Constants.Intake.ID);
        intake.setInverted(true);
        intake.setNeutralMode(NeutralModeValue.Brake);
    }

    public void setIntake(double speed)
    {
        intake.set(speed);
    }
}
