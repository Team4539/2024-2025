package frc.robot.commands.Swerve;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve;

public class setShooter extends Command 
{
    private final Swerve s_Swerve;
    private double m_speed;

    public setShooter(double speed, Swerve subsystem) 
    {
        m_speed = speed;
        s_Swerve = subsystem;

    }
    
    @Override
    public void initialize() {}
    @Override
    public void execute() 
    {
        s_Swerve.setIntake(-m_speed);
        try {
            TimeUnit.SECONDS.wait(1);
        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        s_Swerve.setIntake(m_speed);
        s_Swerve.setShooter(m_speed);
    }
    @Override
    public void end(boolean interrupted) 
    {
        s_Swerve.setShooter(0.0);
    }
    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}