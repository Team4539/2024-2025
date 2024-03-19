package frc.robot.Commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSubsystem;

public class setHeadTo extends Command 
{
    private final ArmSubsystem m_arm;
    private final PIDController pidController;
    private final String m_command;
    private double m_target;
    private double fixedOutput;

    public setHeadTo(double targetrot, ArmSubsystem subsystem, String command) 
    {
        addRequirements(subsystem);
        m_arm = subsystem;
        m_target = targetrot;
        m_command = command;
        pidController = new PIDController(0.175, 0.002, 0.0); // TODO: Adjust these values as needed

    }

    @Override
    public void initialize() 
    {
        
    }

    @Override
    public void execute() 
    {
        double encoder = m_arm.getHeadEncoder();
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
            m_arm.setHead(-fixedOutput*.65); 
        }
        else if (encoder < m_target) //me go up
        {
            m_arm.setHead(-fixedOutput); 

        }
        else //stay
        {
            m_arm.setHead(0);
        }
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}