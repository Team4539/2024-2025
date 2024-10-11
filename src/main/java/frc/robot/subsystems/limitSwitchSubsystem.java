package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class limitSwitchSubsystem extends SubsystemBase
{
    private DigitalInput limitswitch;
    private final TalonFX Motor1 = new TalonFX(1);

    
    public limitSwitchSubsystem()
    {
        limitswitch = new DigitalInput(Constants.Intake.limit_id);
    }

    public void periodic()
    {
        SmartDashboard.putBoolean("Limit Switch", getSwitch());
        SmartDashboard.putString("Motor temp", ((Motor1.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");

    }

    public boolean getSwitch()
    {
        return !limitswitch.get();
    }
}
