package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class ArmSubsystem extends SubsystemBase 
{
    private CANSparkMax arm;
    private CANSparkMax armInverted;
    private DutyCycleEncoder armEncoder;

    public ArmSubsystem() 
    {
        arm = new CANSparkMax(Constants.Arm.armID, MotorType.kBrushless);
        armInverted = new CANSparkMax(Constants.Arm.armInvertedID, MotorType.kBrushless);
        armEncoder = new DutyCycleEncoder(Constants.Arm.armEncoder);
        armInverted.setInverted(true);
        arm.setInverted(false);
        arm.setIdleMode(IdleMode.kBrake);
        armInverted.setIdleMode(IdleMode.kBrake);
    }

    @Override
    public void periodic() 
    {
        SmartDashboard.putNumber("Arm Encoder", (armEncoder.getDistance() * 100) - 55.94);
    }

    @Override
    public void simulationPeriodic() {}

    public void setArm(double speed)
    {
        // if speed is not zero
        if (speed != 0)
        {
            if (ClimberSubsystem.armOverride)
            {
                arm.set(speed*.3);
                armInverted.set(speed*.3);
            }
            else
            {
                // if rotations is greater than minimum and less than Maximum
                if ((armEncoder.getDistance() * 100) - 55.94 > Constants.Arm.armMin && (armEncoder.getDistance() * 100) - 55.94 < Constants.Arm.armMax)
                {
                    // run normal
                    arm.set(speed*.3);
                    armInverted.set(speed*.3);
                }

                // if rotations is less than miminum
                else if ((armEncoder.getDistance() * 100) - 55.94 < Constants.Arm.armMin)
                {
                    // run inverted to push it out at minimum power
                    arm.set(-0.05);
                    armInverted.set(-0.05);
                } 

                //if rotations is greater tham Maximum
                else if((armEncoder.getDistance() * 100) - 55.94 > Constants.Arm.armMax)
                {
                    //run to push in a minimum power
                    arm.set(0.05);
                    armInverted.set(0.05);
                }
            }
        }
        // if speed is zero
        else
        {
            // set zero
            arm.set(0);
            armInverted.set(0);
        }
    }

    /*public void resetEncoder()
    {
        armEncoder.reset();
    }*/
    
    public double getEncoder()
    {
        return (armEncoder.getDistance() * 100) - 55.94;
    }
}