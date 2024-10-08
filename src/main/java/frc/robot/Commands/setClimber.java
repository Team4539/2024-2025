package frc.robot.Commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.LimelightHelpers;

public class setClimber extends Command 
{
    private final ClimberSubsystem m_climber;
    private final double m_speed;

    public setClimber(double speed, ClimberSubsystem subsystem) 
    {
        m_speed = speed;
        m_climber = subsystem;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() 
    {
        m_climber.setClimber(m_speed);
        LimelightHelpers.setLEDMode_ForceBlink("limelight");

    }
    @Override
    public void end(boolean interrupted) 
    {
        m_climber.setClimber(0.0);
    }
    
    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}