package frc.robot.Commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSubsystem;

public class setArmTo extends Command 
{
    private final ArmSubsystem m_arm;
    private double m_target;
    private final PIDController pidController;

    public setArmTo(double targetrot, ArmSubsystem subsystem) 
    {
        addRequirements(subsystem);
        m_arm = subsystem;
        m_target = targetrot;
        pidController = new PIDController(0.075, 0.0, 0.0); // Adjust these values as needed

    }

    @Override
    public void initialize() 
    {

    }

    @Override
    public void execute() 
    {
        double encoder = m_arm.getEncoder();
        double output = pidController.calculate(encoder, m_target);

        if (encoder > m_target) // needs to go down
        {
            m_arm.setArm(output); 
        }
        else if (encoder < m_target)
        {
            m_arm.setArm(-output/2); 
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