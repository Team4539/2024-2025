package frc.robot.Commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HeadSubsystem;
import frc.robot.subsystems.LimelightHelpers;

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
        pidController = new PIDController(12, 0.0, 0);

    }

    @Override
    public void initialize() {}

    @Override
    public void execute() 
    {
        double encoder = m_head.getHeadEncoder();
        double output = pidController.calculate(encoder, m_target);

        if (output > 1)
        {
            fixedOutput = 1; 
        }
        else 
        {
            fixedOutput = output;
        }

        if (encoder > m_target)
        {
            m_head.setHead(-fixedOutput); 

        }
        else if (encoder < m_target)
        {
            m_head.setHead(-fixedOutput);
 

        }
        else
        {
            m_head.setHead(0);
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        m_head.setHead(0);
        LimelightHelpers.setLEDMode_ForceOff("limelight");
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}