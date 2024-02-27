package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LedCommSubsystem extends SubsystemBase{
    private PWM pwm;

    public LedCommSubsystem()
    {
        pwm = new PWM(Constants.arduinoCOMs.arduinoCOM);
    }
    public void periodic(){
        pwm.setPulseTimeMicroseconds(1);
    }

    public void setLed(double speed)
    {
        pwm.setSpeed(1.0);
    }
}
