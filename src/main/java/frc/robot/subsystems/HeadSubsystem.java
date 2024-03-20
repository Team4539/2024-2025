package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class HeadSubsystem extends SubsystemBase 
{
    private CANSparkMax head;
    private DutyCycleEncoder headEncoder;

    public HeadSubsystem() 
    {
        headEncoder = new DutyCycleEncoder(Constants.Arm.headEncoder);
        head = new CANSparkMax(Constants.Arm.HEAD_ID, MotorType.kBrushless);
        head.setInverted(false);
        head.setIdleMode(IdleMode.kBrake);
    }

    @Override
    public void periodic() 
    {
        SmartDashboard.putNumber("Head Encoder", (headEncoder.getDistance() * 100) - 90); // TODO: find offest
    }

    @Override
    public void simulationPeriodic() {}

    public void setHead(double speed)
    {
        // if speed is not zero
        if (speed != 0)
        {
            if (ClimberSubsystem.armOverride)
            {
                head.set(speed*.3);
            }
            else
            {
                // if rotations is greater than minimum and less than Maximum
                if ((headEncoder.getDistance() * 100) - 90 > Constants.Arm.headMin && (headEncoder.getDistance() * 100) - 90 < Constants.Arm.headMax)
                {
                    // run normal
                    head.set(speed*.3);
                }

                // if rotations is less than miminum
                else if ((headEncoder.getDistance() * 100) - 90 < Constants.Arm.headMin)
                {
                    // run inverted to push it out at minimum power
                    head.set(-0.05);
                } 

                //if rotations is greater tham Maximum
                else if((headEncoder.getDistance() * 100) - 90 > Constants.Arm.headMax)
                {
                    //run to push in a minimum power
                    head.set(0.05);
                }
            }
        }
        // if speed is zero
        else
        {
            // set zero
            head.set(0);
        }
    }

    /*public void setHead(double speed)
    {
        // if speed is not zero
        if (speed != 0)
        {
            head.set(speed*.3);
        }
        // if speed is zero
        else
        {
            // set zero
            head.set(0);
        }
    }*/

    /*public void resetEncoder()
    {
        armEncoder.reset();
    }*/

    public double getHeadEncoder()
    {
        return (headEncoder.getDistance() * 100) - 90; //TODO: also put the head offset here!
    }
}