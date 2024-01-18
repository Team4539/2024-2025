package frc.robot.commands.Swerve;
import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Swerve;

public class setShooter extends Command 
{
    private final Swerve s_Swerve;
   
    private double m_shooterSpeed;
   

    public setShooter(double intakeSpeed, double shooterSpeed, double intakeResetTime, double spoolShooterSpeed, Swerve subsystem) 
    {
       
        m_shooterSpeed = shooterSpeed;
      
        s_Swerve = subsystem;
    }
    
    @Override
    public void initialize() {}

  
    @Override
    public void execute() 
    {
        s_Swerve.setShooter(m_shooterSpeed);
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