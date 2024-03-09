package frc.robot.Commands;
import java.util.function.DoubleSupplier;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;

public class setArm extends Command 
{
    private final ArmSubsystem m_arm;
    private final DoubleSupplier m_speed;
    private double speedAxis;
    
    public setArm(DoubleSupplier speed, ArmSubsystem subsystem) 
    {
        addRequirements(subsystem);
        m_speed = speed;
        m_arm = subsystem;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() 
    {
        speedAxis = MathUtil.applyDeadband(m_speed.getAsDouble(), Constants.stickDeadband);
        m_arm.setArm(speedAxis);
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}