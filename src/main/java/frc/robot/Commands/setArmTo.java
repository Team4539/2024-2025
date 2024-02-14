package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSubsystem;

public class setArmTo extends Command 
{
    private final ArmSubsystem m_arm;
    private double m_target;

    public setArmTo(double targetrot, ArmSubsystem subsystem) 
    {
        addRequirements(subsystem);
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
        if (encoder > m_target+.5) // needs to go down
        {
            m_arm.setArm(0.1); 
        }
        else if (encoder < m_target-1)
        {
            m_arm.setArm(-0.75); 
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