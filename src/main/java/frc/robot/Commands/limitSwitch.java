package frc.robot.Commands;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.limitSwitchSubsystem;

public class limitSwitch extends Command 
{
    private boolean finished;
    private limitSwitchSubsystem m_switch;

    public limitSwitch(limitSwitchSubsystem subsystem) 
    {
        addRequirements(subsystem);
        m_switch = subsystem;
    }

    @Override
    public void initialize() 
    {
        finished = false;
    }

    @Override
    public void execute() 
    {
        boolean result = m_switch.getSwitch();
        SmartDashboard.putBoolean("Note Detected", result);
        if (result)
        {
            finished = true;
            isFinished();
        }
    }

    @Override
    public void end(boolean interrupted) 
    {
        isFinished();
    }

    @Override
    public boolean isFinished() { DriverStation.reportWarning("############################# limitSwitch finished! #############################", false); return finished; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}