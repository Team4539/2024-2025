package frc.robot.commands.Swerve;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Swerve;

public class setArm extends Command 
{
    private final Swerve s_Swerve;
    private final  double m_speed;
    private DoubleSupplier armSup;
    private Object armVal;
    public setArm(double speed, Swerve subsystem) 
    {
        m_speed = Constants.Swerve.intakeSpeed;
        s_Swerve = subsystem;
        this.armVal = armVal;
    }

    @Override
    public void initialize() {}
    @Override
    public void execute() 
    {
       /* Get Values, Deadband*/
       
        double armVal = MathUtil.applyDeadband(armSup.getAsDouble(), Constants.stickDeadband);
    }
    {
 
   //TODO: help with arm move 
   
    }
    @Override
    public void end(boolean interrupted) 
    {
        s_Swerve.setIntake(0.0);
    }
    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}