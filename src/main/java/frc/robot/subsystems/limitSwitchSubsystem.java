package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class limitSwitchSubsystem extends SubsystemBase
{
    private DigitalInput limitswitch;
    
    public limitSwitchSubsystem()
    {
        limitswitch = new DigitalInput(Constants.Intake.limit_id);
    }

    public boolean getSwitch()
    {
        // if true, limit switch is tripped; else it has not been tripped
        return !limitswitch.get();
    }
}
