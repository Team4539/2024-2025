package frc.robot.commands.Swerve;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Arm;

public class setArm extends Command 
{
    private final Arm m_arm;
    private double speedAxis;
    private final DoubleSupplier m_speed;

    public setArm(DoubleSupplier speed, Arm subsystem) 
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

    // @Override
    // public void end(boolean interrupted) 
    // {
    //     m_arm.setArm(m_speed);
    // }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}