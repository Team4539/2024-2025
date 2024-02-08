package frc.robot.Commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;

public class setArmTo extends Command 
{
    private final ArmSubsystem m_arm;
    private final DoubleSupplier m_speed;
    private double speedAxis;
    private double m_target;

    public setArmTo(double targetrot, DoubleSupplier speed, ArmSubsystem subsystem) 
    {
        addRequirements(subsystem);
        m_speed = speed;
        m_arm = subsystem;
        m_target = targetrot;
    }

    @Override
    public void initialize() 
    {

    }

    @Override
    public void execute() 
    {
        double encoder = m_arm.getEncoder();
        if (encoder > m_target) // needs to go down
        {
            m_arm.setArm(-0.3); // TODO: test this value
        }
        else if (encoder < m_target)
        {
            m_arm.setArm(0.3); // TODO: test this value
        }
        else
        {
            m_arm.setArm(0);
        }
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}