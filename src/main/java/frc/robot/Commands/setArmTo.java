package frc.robot.Commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSubsystem;

public class setArmTo extends Command 
{
    private final ArmSubsystem m_arm;
    private double m_target;
    private double fixedOutput;
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
        if (output > 1)
        {
            fixedOutput = 1; 
        }
        else 
        {
            fixedOutput = output;
        }

        if (encoder > m_target) // needs to go down
        {
            m_arm.setArm(-fixedOutput); 
        }
        else if (encoder < m_target) //me go up
        {
            m_arm.setArm(-fixedOutput); 

        }
        else //stay
        {
            m_arm.setArm(0);
        }
         SmartDashboard.putNumber("output", output);

    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}