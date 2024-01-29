package frc.robot.commands.Swerve;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve;

public class setIntake extends Command 
{
    private final Swerve s_Swerve;
    private final double m_speed;

    public setIntake(double speed, Swerve subsystem) 
    {
        m_speed = speed;
        s_Swerve = subsystem;
    }

    @Override
    public void initialize() {  }

    @Override
    public void execute() 
    {
        s_Swerve.setIntake(m_speed);
    }
    @Override
    public void end(boolean interrupted) 
    {
        SmartDashboard.putNumber("Intake Speed", s_Swerve.intake.get());
        s_Swerve.setIntake(0.0);
    }
    
    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}