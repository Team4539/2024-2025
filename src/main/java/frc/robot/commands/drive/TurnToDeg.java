package frc.robot.commands.drive;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve;
public class TurnToDeg extends Command 
{
    private final Swerve m_swerve;
    private double m_target;
    public TurnToDeg(Swerve subsystem, int targetangle) 
    {
        m_target = targetangle;
        m_swerve = subsystem;
        addRequirements(m_swerve);
    }
    @Override
    public void initialize() 
    {
        m_swerve.zeroGyro();
    }
    @Override
    public void execute() 
    {
        m_swerve.drive(new Translation2d(0,0), m_target, false, false);
    }
    @Override
    public void end(boolean interrupted) 
    {
        m_swerve.drive(new Translation2d(0,0), 0, false, false);
    }

    @Override
    public boolean isFinished() {
        int getHeadingInt = (int) Math.round(m_swerve.getYaw().getDegrees());
        if (getHeadingInt == m_target)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}