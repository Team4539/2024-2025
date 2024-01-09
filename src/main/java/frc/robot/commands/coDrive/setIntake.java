package frc.robot.commands.coDrive;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve;

public class setIntake extends Command
{
    private final Swerve m_driveTrain;
    private double m_speed;

    public setIntake(double speed, Swerve subsystem) 
    {
        m_speed = speed;
        m_driveTrain = subsystem;
    }
    
    @Override
    public void initialize() {
    }
    @Override
    public void execute() {
        m_driveTrain.setIntake(m_speed);
    }
    @Override
    public void end(boolean interrupted) {
        m_driveTrain.setIntake(0.0);
    }
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
