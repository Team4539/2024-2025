package frc.robot.subsystems;

import javax.xml.crypto.Data;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.Power;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
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
    PowerDistribution pdp = new PowerDistribution(0, ModuleType.kCTRE);
    private final double MOTOR1_temp = Motor1.getDeviceTemp().getValue();
    private final double MOTOR3_temp = Motor3.getDeviceTemp().getValue();
    private final double MOTOR6_temp = Motor6.getDeviceTemp().getValue();
    private final double MOTOR7_temp = Motor7.getDeviceTemp().getValue();
    private boolean MOD0OT = false;
    private boolean MOD1OT = false;
    private boolean MOD2OT = false;
    private boolean MOD3OT = false;
    private boolean BROWNOUT = false;

    
    
    public limitSwitchSubsystem()
    {
        limitswitch = new DigitalInput(Constants.Intake.limit_id);
    }

    public void periodic()
    {
        if(Motor1.getProcessorTemp().getValue() > 90){
            MOD0OT = false; }
        else{
            MOD0OT = true;}
        if(Motor3.getProcessorTemp().getValue() > 90){
            MOD1OT = false; }
        else{
            MOD1OT = true;}
        if(Motor6.getProcessorTemp().getValue() > 90){
            MOD2OT = false; }
        else{
            MOD2OT = true;}
        if(Motor7.getProcessorTemp().getValue() > 90){
            MOD3OT = false; }
        else{
            MOD3OT = true;}
        if(pdp.getVoltage() < 6.8){ 
            BROWNOUT = true;}

        



        SmartDashboard.putBoolean("Limit Switch", getSwitch());
        SmartDashboard.putString("Module 0 Drive", ((Motor1.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 0 Turn", ((Motor2.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 1 Drive", ((Motor3.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 1 Turn", ((Motor4.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 2 Drive", ((Motor6.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 2 Turn", ((Motor5.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 3 Drive", ((Motor7.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Module 3 Turn", ((Motor8.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Intake Motor", ((Motor14.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Shooter Motor 1", ((Motor15.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putString("Shooter Motor 2", ((Motor16.getProcessorTemp().getValue() * 9/5) + 32) + "\u00B0F");
        SmartDashboard.putBoolean("MOD 0 Over Temp", MOD0OT);
        SmartDashboard.putBoolean("MOD 1 Over Temp", MOD1OT);
        SmartDashboard.putBoolean("MOD 2 Over Temp", MOD2OT);
        SmartDashboard.putBoolean("MOD 3 Over Temp", MOD3OT);
        SmartDashboard.putBoolean("BROWNOUT HAPPENED", BROWNOUT);
        SmartDashboard.putNumber("BatVolt", pdp.getVoltage());

       


    }
    

    public boolean getSwitch()
    {
        return !limitswitch.get();
    }
}
