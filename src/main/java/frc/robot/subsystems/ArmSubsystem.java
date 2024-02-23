package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ArmSubsystem extends SubsystemBase 
{
    private TalonFX arm;
    private TalonFX armInverted;
    private DutyCycleEncoder armEncoder;
    private double armEncoderVal;

    public ArmSubsystem() 
    {
        arm = new TalonFX(Constants.Arm.armID);
        armInverted = new TalonFX(Constants.Arm.armInvertedID);
        armEncoder = new DutyCycleEncoder(Constants.Arm.armEncoder);
        armInverted.setInverted(true);
        arm.setInverted(false);
        arm.setNeutralMode(NeutralModeValue.Brake);
        armInverted.setNeutralMode(NeutralModeValue.Brake);
        
        
    }

    @Override
    public void periodic() 
    {
        SmartDashboard.putNumber("Arm Encoder", (armEncoder.getDistance() *100) - 4.68);
        arm.feed();
    }

    @Override
    public void simulationPeriodic() {}

    public void setArm(double speed)
    {
        // if speed is not zero
        if (speed != 0)
        {
            // if rotations is greater than minimum and less than Maximum
            if (armEncoder.getDistance() > Constants.Arm.armMin && armEncoder.getDistance() < Constants.Arm.armMax)
            {
                // run normal
                arm.set(speed*.3);
                armInverted.set(speed*.3);
            }

            // if rotations is less than miminum
            else if (armEncoder.getDistance() < Constants.Arm.armMin)
            {
                // run inverted to push it out at minimum power
                arm.set(-0.1);
                armInverted.set(-0.1);
            } 

            //if rotations is greater tham Maximum
            else if(armEncoder.getDistance() > Constants.Arm.armMax )
            {
                //run to push in a minimum power
                arm.set(0.1);
                armInverted.set(0.1);
            }
        }
        // if speed is zero
        else
        {
            // set zero
            arm.set(0);
            armInverted.set(0);
        }
    }

    /*public void resetEncoder()
    {
        armEncoder.reset();
    }*/
    
    public double getEncoder()
    {
        return armEncoder.getDistance();
    }
}