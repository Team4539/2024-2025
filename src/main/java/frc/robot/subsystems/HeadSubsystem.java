package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class HeadSubsystem extends SubsystemBase 
{
    private TalonFX head;
    private DutyCycleEncoder headEncoder;
    private Servo servo;

    public HeadSubsystem() 
    {
        headEncoder = new DutyCycleEncoder(Constants.Arm.headEncoder);
        head = new TalonFX(Constants.Arm.HEAD_ID);
        head.setInverted(false);
        head.setNeutralMode(NeutralModeValue.Brake);
        servo = new Servo(Constants.Servo.id);
        ServoHome();
    }

    @Override
    public void periodic() {}

    @Override
    public void simulationPeriodic() {}

    public void ServoBomb()
    {
        servo.set(Constants.Servo.position);
    }

    public void ServoHome()
    {
        servo.set(Constants.Servo.home);
    }

    public void setHead(double speed)
    {
        if (speed != 0)
        {
            if (ClimberSubsystem.armOverride)
            {
                head.set(speed*.6);
            }
            else
            {
                if ((headEncoder.getAbsolutePosition() + Constants.Arm.headOffset) > Constants.Arm.headMin && (headEncoder.getAbsolutePosition() + Constants.Arm.headOffset) < Constants.Arm.headMax)
                {
                    head.set(speed*.6);
                }
                else if ((headEncoder.getAbsolutePosition() + Constants.Arm.headOffset) < Constants.Arm.headMin)
                {
                    head.set(-0.05);
                } 
                else if ((headEncoder.getAbsolutePosition() + Constants.Arm.headOffset) > Constants.Arm.headMax)
                {
                    head.set(0.05);
                }
            }
        }
        else
        {
            head.set(0);
        }
    }

    public double getHeadEncoder()
    {
        return (headEncoder.getAbsolutePosition() + Constants.Arm.headOffset);
    }
}