package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class Climb extends SubsystemBase 
{
    private TalonFX climb;

    public Climb() 
    {
        climb = new TalonFX(Constants.Climb.climbID);
        climb.setNeutralMode(NeutralModeValue.Brake);
    }

    @Override
    public void periodic() 
    {
        SmartDashboard.putNumber("climb Encoder", -climb.getRotorPosition().getValueAsDouble());
        climb.feed();
    }

    @Override
    public void simulationPeriodic() {}

    public void setClimb(double speed)
    {
        // if speed is not zero
        if (speed != 0)
        {
            // if rotations is greater than minimum
            if (-climb.getRotorPosition().getValueAsDouble() > Constants.Climb.climbMin)
            {
                // run normal
                climb.set(speed*.1);
            }

            // if rotations is less than miminum
            else if (-climb.getRotorPosition().getValueAsDouble() < Constants.Climb.climbMin)
            {
                // run inverted to push it out at minimum power
                climb.set(-0.1);
            }
        }
        // if speed is zero
        else
        {
            // set zero
            climb.set(0);
        }
    }

    public void resetEncoder()
    {
        climb.setPosition(0);
    }
    
    public double getEncoder()
    {
        return -climb.getRotorPosition().getValueAsDouble();
    }
}