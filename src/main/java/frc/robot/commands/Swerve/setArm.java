package frc.robot.commands.Swerve;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;

public class setArm extends Command 
{
    private final Arm m_arm;
    private final  double m_speed;

    public setArm(double speed, Arm subsystem) 
    {
        m_speed = speed;
        m_arm = subsystem;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() 
    {
        m_arm.setArm(m_speed);
    }

    @Override
    public void end(boolean interrupted) 
    {
        m_arm.setArm(m_speed);
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}