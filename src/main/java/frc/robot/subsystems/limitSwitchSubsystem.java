package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class limitSwitchSubsystem extends SubsystemBase
{
    private DigitalInput limitswitch;
    
    public limitSwitchSubsystem()
    {
        limitswitch = new DigitalInput(Constants.Intake.limit_id);
    }

    public void periodic()
    {
        SmartDashboard.putBoolean("Limit Switch", getSwitch());
    }

    public boolean getSwitch()
    {
        return !limitswitch.get();
    }
}
