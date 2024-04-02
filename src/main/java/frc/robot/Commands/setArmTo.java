package frc.robot.Commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSubsystem;

public class setArmTo extends Command 
{
    private final ArmSubsystem m_arm;
    private final PIDController pidController;
    private final String m_command;
    private double m_target;
    private double fixedOutput;

    public setArmTo(double targetrot, ArmSubsystem subsystem, String command) 
    {
        addRequirements(subsystem);
        m_arm = subsystem;
        m_target = targetrot;
        m_command = command;
        pidController = new PIDController(15, 0, 0.0); // Adjust these values as needed

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
        
        SmartDashboard.putNumber("output", output);
        SmartDashboard.putString("Command", m_command.toString());

        if (output > 1)
        {
            fixedOutput = 1; 
        }
        else 
        {
            fixedOutput = output;
        }

        if (encoder > m_target) // go down
        {
            m_arm.setArm(fixedOutput*.65); 
        }
        else if (encoder < m_target) //me go up
        {
            m_arm.setArm(fixedOutput); 

        }
        else //stay
        {
            m_arm.setArm(0);
        }
    }

    @Override
    public void end(boolean interrupted) 
    {
        m_arm.setArm(0);
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}