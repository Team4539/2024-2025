package frc.robot.Commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VisionSubsystem;

public class aimVision extends Command 
{
    private final VisionSubsystem m_vision;
    
    public aimVision(VisionSubsystem subsystem) 
    {
        addRequirements(subsystem);
        m_vision = subsystem;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() 
    {
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}