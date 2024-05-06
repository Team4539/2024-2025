package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberSubsystem extends SubsystemBase
{
    public TalonFX climber;
    public static boolean armOverride = false;

    public ClimberSubsystem()
    {
        climber = new TalonFX(Constants.Climber.ID);
        climber.setInverted(true);
        climber.setNeutralMode(NeutralModeValue.Brake);
    }

    @Override
    public void periodic() {}

    public void setOverride(boolean value)
    {
        armOverride = value;
    }

    public void setClimber(double speed)
    {
        if (speed != 0)
        {
            if (-climber.getRotorPosition().getValueAsDouble() > Constants.Climber.climberMin && -climber.getRotorPosition().getValueAsDouble() < Constants.Climber.climberMax)
            {
                climber.set(-speed);
            }
            else if (-climber.getRotorPosition().getValueAsDouble() < Constants.Climber.climberMin)
            {
                climber.set(-0.1);
            } 
            else if(-climber.getRotorPosition().getValueAsDouble() > Constants.Climber.climberMax )
            {
                climber.set(0.1);
            }
        }
        else
        {
            climber.set(0);
        }
    }
}