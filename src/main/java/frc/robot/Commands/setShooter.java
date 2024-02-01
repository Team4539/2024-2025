package frc.robot.Commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class setShooter extends Command 
{
    private ShooterSubsystem m_shooter;
    private double m_speed;
   
    public setShooter(double speed, ShooterSubsystem subsystem) 
    {
        m_speed = speed;
        m_shooter = subsystem;
    }
    
    @Override
    public void initialize() 
    {
        m_shooter.override = true;
    }

    @Override
    public void execute() 
    {
        m_shooter.setShooter(m_speed);
    }

    @Override
    public void end(boolean interrupted) 
    {
        m_shooter.setShooter(0.0);
        m_shooter.override = false;
    }
    
    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}