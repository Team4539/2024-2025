package frc.robot.Commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
//import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.HeadSubsystem;

public class setHeadTo extends Command 
{
    private final HeadSubsystem m_head;
    private final PIDController pidController;
    private final String m_command;
    private double m_target;
    private double fixedOutput;

    public setHeadTo(double targetrot, HeadSubsystem subsystem, String command) 
    {
        addRequirements(subsystem);
        m_head = subsystem;
        m_target = targetrot;
        m_command = command;
        pidController = new PIDController(10, 0.0, 0.5);

    }

    @Override
    public void initialize() 
    {
        
    }

    @Override
    public void execute() 
    {
        double encoder = m_head.getHeadEncoder();
        double output = pidController.calculate(encoder, m_target);
        
        SmartDashboard.putNumber("head output", output);
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
            m_head.setHead(-fixedOutput); 
        }
        else if (encoder < m_target) //me go up
        {
            m_head.setHead(-fixedOutput); 

        }
        else //stay
        {
            m_head.setHead(0);
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        m_head.setHead(0);
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}