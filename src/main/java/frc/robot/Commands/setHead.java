package frc.robot.Commands;
import java.util.function.DoubleSupplier;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.HeadSubsystem;

public class setHead extends Command 
{
    private final HeadSubsystem m_head;
    private final DoubleSupplier m_speed;
    private double speedAxis;
    
    public setHead(DoubleSupplier speed, HeadSubsystem subsystem) 
    {
        addRequirements(subsystem);
        m_speed = speed;
        m_head = subsystem;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() 
    {
        speedAxis = MathUtil.applyDeadband(m_speed.getAsDouble(), Constants.stickDeadband);
        m_head.setHead(speedAxis);
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}