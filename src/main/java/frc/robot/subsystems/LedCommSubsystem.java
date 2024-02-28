package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LedCommSubsystem extends SubsystemBase{
    private PWM pwmSnd;
    private DigitalInput pwmRcv;

    public LedCommSubsystem()
    {
        pwmSnd = new PWM(Constants.arduinoCOMs.arduinoSend);
        pwmRcv = new DigitalInput(Constants.arduinoCOMs.arduinoRcv);
        
    }
    public void periodic(){
        if (pwmRcv.get() == false){
            SmartDashboard.putBoolean("NOTE", true);
        }else {
            SmartDashboard.putBoolean("NOTE", false);
        }
        SmartDashboard.putBoolean("DATA", pwmRcv.get());
    }   
    public void setLed(double speed)
    {
        pwmSnd.setSpeed(speed);
    }
}
