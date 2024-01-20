package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class Arm extends SubsystemBase 
{
    private TalonFX arm;
    private TalonFX armInverted;

    public Arm() 
    {
        arm = new TalonFX(Constants.Arm.armID);
        armInverted = new TalonFX(Constants.Arm.armInvertedID);
        armInverted.setInverted(true);
        arm.setInverted(false);
        arm.setNeutralMode(NeutralModeValue.Brake);
        armInverted.setNeutralMode(NeutralModeValue.Brake);
    }

    @Override
    public void periodic() 
    {
        SmartDashboard.putNumber("Arm Encoder", arm.getRotorPosition().getValueAsDouble());
        arm.feed();
    }

    @Override
    public void simulationPeriodic() {}

    public void setArm(double speed)
    {
        arm.set(speed);
        armInverted.set(speed);
    }

    public void resetEncoder()
    {
        arm.setPosition(0);
        armInverted.setPosition(0);
    }
    
    public double getEncoder()
    {
        return arm.getRotorPosition().getValueAsDouble();
    }

    public void setDefaultCommand(frc.robot.commands.Swerve.setArm setArm, Arm m_arm) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDefaultCommand'");
    }
}