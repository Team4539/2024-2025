package frc.robot.Commands;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class setIntake extends Command 
{
    private final IntakeSubsystem m_intake;
    private final double m_speed;

    public setIntake(double speed, IntakeSubsystem subsystem) 
    {
        m_speed = speed;
        m_intake = subsystem;
    }

    @Override
    public void initialize() {  }

    @Override
    public void execute() 
    {
        m_intake.setIntake(m_speed);
    }
    @Override
    public void end(boolean interrupted) 
    {
        SmartDashboard.putNumber("Intake Speed", m_intake.intake.get());
        m_intake.setIntake(0.0);
    }
    
    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}