package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase
{
    public TalonFX intake;
    public CANSparkMax indexer;
    private DigitalInput limitswitch;
    
    public IntakeSubsystem()
    {
        intake = new TalonFX(Constants.Intake.ID);
        intake.setInverted(true);
        intake.setNeutralMode(NeutralModeValue.Brake);
        indexer = new CANSparkMax(Constants.Intake.INDEXER_ID, MotorType.kBrushless);
        indexer.setInverted(true);
        indexer.setIdleMode(IdleMode.kBrake);
        limitswitch = new DigitalInput(Constants.Intake.limit_id);
    }

    public void setIntake(double speed)
    {
        intake.set(speed);
        indexer.set(speed);
    }

    public boolean getSwitch()
    {
        // if true, limit switch is tripped; else it has not been tripped
        return limitswitch.get();
    }
}
