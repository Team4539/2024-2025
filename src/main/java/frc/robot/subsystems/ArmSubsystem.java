package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ArmSubsystem extends SubsystemBase 
{
    private TalonFX arm;
    private TalonFX armInverted;
    private DutyCycleEncoder armEncoder;

    public ArmSubsystem() 
    {
        arm = new TalonFX(Constants.Arm.armID);
        armInverted = new TalonFX(Constants.Arm.armInvertedID);
        armEncoder = new DutyCycleEncoder(Constants.Arm.armEncoder);
        armInverted.setInverted(true);
        arm.setInverted(false);
        arm.setNeutralMode(NeutralModeValue.Brake);
        armInverted.setNeutralMode(NeutralModeValue.Brake);
    }

    @Override
    public void periodic() {}

    @Override
    public void simulationPeriodic() {}

    public void setArm(double speed)
    {
        if (speed != 0)
        {
            if (ClimberSubsystem.armOverride)
            {
                arm.set(speed*.3);
                armInverted.set(speed*.3);
            }
            else
            {
                if ((armEncoder.getAbsolutePosition() + Constants.Arm.armOffset) > Constants.Arm.armMin && (armEncoder.getAbsolutePosition() + Constants.Arm.armOffset) < Constants.Arm.armMax)
                {
                    arm.set(speed*-.3);
                    armInverted.set(speed*-.3);
                }
                else if ((armEncoder.getAbsolutePosition() + Constants.Arm.armOffset) < Constants.Arm.armMin)
                {
                    arm.set(-0.05);
                    armInverted.set(-0.05);
                } 
                else if ((armEncoder.getAbsolutePosition() + Constants.Arm.armOffset) > Constants.Arm.armMax)
                {
                    arm.set(0.05);
                    armInverted.set(0.05);
                }
            }
        }
        else
        {
            arm.set(0);
            armInverted.set(0);
        }
    }
    
    public double getEncoder()
    {
        return (armEncoder.getAbsolutePosition() + Constants.Arm.armOffset);
    }
}