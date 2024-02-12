package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    @Override
    public void periodic() 
    {
        SmartDashboard.putNumber("Climber Encoder", -climber.getRotorPosition().getValueAsDouble());
        climber.feed();
    }

    public void setClimber(double speed)
    {
        //climber.set(speed);
    if (speed != 0)
        {
            // if rotations is greater than minimum and less than Maximum
            if (-climber.getRotorPosition().getValueAsDouble() > Constants.Climber.climberMin && -climber.getRotorPosition().getValueAsDouble() < Constants.Climber.climberMax)
            {
                // run normal
                climber.set(speed*.75);
            }

            // if rotations is less than miminum
            else if (-climber.getRotorPosition().getValueAsDouble() < Constants.Climber.climberMin)
            {
                // run inverted to push it out at minimum power
                climber.set(-0.1);
            } 

            //if rotations is greater tham Maximum
            else if(-climber.getRotorPosition().getValueAsDouble() > Constants.Climber.climberMax )
            {
                //run to push in a minimum power
                climber.set(0.1);
            }
        }
        // if speed is zero
        else
        {
            // set zero
            climber.set(0);
        }
    }
}
