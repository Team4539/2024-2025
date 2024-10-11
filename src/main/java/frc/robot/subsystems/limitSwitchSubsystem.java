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
    private final TalonFX Motor2 = new TalonFX(2);
    private final TalonFX Motor3 = new TalonFX(3);
    private final TalonFX Motor4 = new TalonFX(4);
    private final TalonFX Motor5 = new TalonFX(5);
    private final TalonFX Motor6 = new TalonFX(6); 
    private final TalonFX Motor7 = new TalonFX(7);
    private final TalonFX Motor8 = new TalonFX(8);
    private final TalonFX Motor14 = new TalonFX(14);
    private final TalonFX Motor15 = new TalonFX(15);
    private final TalonFX Motor16 = new TalonFX(16);


    
    public limitSwitchSubsystem()
    {
        limitswitch = new DigitalInput(Constants.Intake.limit_id);
    }

    public void periodic()
    {
        SmartDashboard.putBoolean("Limit Switch", getSwitch());
        SmartDashboard.putString("Module 0 Drive", ((Motor1.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 0 Turn", ((Motor2.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 1 Drive", ((Motor3.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 1 Turn", ((Motor4.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 2 Drive", ((Motor6.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 2 Turn", ((Motor5.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 3 Drive", ((Motor7.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 3 Turn", ((Motor8.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Intake Motor", ((Motor14.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Shooter Motor 1", ((Motor15.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Shooter Motor 2", ((Motor16.getDeviceTemp().getValue() * 9/5) + 32) + "\u00B0F");

    }

    public boolean getSwitch()
    {
        return !limitswitch.get();
    }
}
