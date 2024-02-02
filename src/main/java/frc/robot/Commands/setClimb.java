package frc.robot.Commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.ClimbSubsystem;

public class setClimb extends Command 
{
    private final ClimbSubsystem m_climb;
    private double speedAxis;
    private final DoubleSupplier m_speed;

    public setClimb(DoubleSupplier speed, ClimbSubsystem subsystem) 
    {
        addRequirements(subsystem);
        m_speed = speed;
        m_climb = subsystem;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() 
    {
        speedAxis = MathUtil.applyDeadband(m_speed.getAsDouble(), Constants.stickDeadband);
        m_climb.setClimb(speedAxis);
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}