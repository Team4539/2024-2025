/*package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LedCommSubsystem extends SubsystemBase{
    private PWM pwmSnd;
    private AnalogInput pwmRcv;

    public LedCommSubsystem()
    {
        // pwmSnd = new PWM(Constants.arduinoCOMs.arduinoSend);
        // pwmRcv = new AnalogInput(Constants.arduinoCOMs.arduinoRcv);
        
    }
    public void periodic(){
        if (pwmRcv.getValue() >= 3000){
            SmartDashboard.putBoolean("NOTE", true);
        }else {
            SmartDashboard.putBoolean("NOTE", false);
        }
    }   
    public void setLed(double speed)
    {
        pwmSnd.setSpeed(speed);
    }
}
*/