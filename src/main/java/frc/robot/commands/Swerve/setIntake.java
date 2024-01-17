package frc.robot.commands.Swerve;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Swerve;

public class setIntake extends Command 
{
    private final Swerve s_Swerve;
    private double m_speed;

    public setIntake(double speed, Swerve subsystem) 
    {
        m_speed = speed;
        s_Swerve = subsystem;
    }
    private boolean isFinished = false;
    @Override
    public void initialize() {}
    @Override
    public void execute() 
    {
        s_Swerve.setIntake(m_speed);

        if (frc.robot.subsystems.Swerve.isOrange() == true) {
            isFinished = true;
        }
        else {
        isFinished = false;
    }
    }
    @Override
    public void end(boolean interrupted) 
    {
        s_Swerve.setIntake(0.0);
    }
    @Override
    public boolean isFinished() { return isFinished; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}