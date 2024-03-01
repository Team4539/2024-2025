package frc.robot.Commands;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class check4Note extends Command 
{
    public check4Note() 
    {
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() 
    {
    }

    @Override
    public boolean isFinished() 
    { 
        return SmartDashboard.getBoolean("NOTE", false); 
    }

    @Override
    public boolean runsWhenDisabled() { return false; }
}