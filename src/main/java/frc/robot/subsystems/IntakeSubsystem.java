package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase
{
    public TalonFX intake;

    public IntakeSubsystem()
    {
        intake = new TalonFX(Constants.Intake.ID);
        intake.setInverted(true);
    }

    public void setIntake(double speed)
    {
        intake.set(speed);
    }
}
