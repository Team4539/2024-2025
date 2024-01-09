package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve;

public class ResetPosition extends Command 
{
    private final Swerve m_swerve;

    public ResetPosition(Swerve mswerve) 
    {
        m_swerve = mswerve;
    }
    
    @Override
    public void initialize() {
        m_swerve.zeroGyro();
    }
    @Override
    public void execute() 
    {

    }
    @Override
    public void end(boolean interrupted) {
    }
    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}