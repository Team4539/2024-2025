package frc.robot.Commands;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.VisionSubsystem;

public class aimVision extends Command 
{
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    
    private final VisionSubsystem m_vision;
    
    private final CommandSwerveDrivetrain m_drive;

    private int m_tag;
    private double m_x;

    private boolean check1;
    private boolean check2;
    private boolean check3;
    private boolean check4;

    public aimVision(int target_tag, double target_x, VisionSubsystem subsystem, CommandSwerveDrivetrain drive_subsystem) 
    {
        addRequirements(subsystem, drive_subsystem);
        m_vision = subsystem;
        m_drive = drive_subsystem;
        m_tag = target_tag;
        m_x = target_x;
    }

    @Override
    public void initialize() 
    {
        check1 = false;
        check2 = false;
        check3 = false;
        check4 = false;
    }

    @Override
    public void execute() 
    {
        var result = m_vision.camera.getLatestResult();
        if (result.hasTargets())
        {
            for (var target: result.getTargets())
            {
                if (target.getFiducialId() == m_tag)
                {
                    double tx = target.getBestCameraToTarget().getX(); // target is 1
                    double ty = target.getBestCameraToTarget().getY(); // target is between -Constants.Vision.Y_GOAL and Constants.Vision.Y_GOAL
                    double tangle = Units.radiansToDegrees(target.getBestCameraToTarget().getRotation().getAngle()); // target is between Constants.Vision.A_GOAL1 to Constants.Vision.A_GOAL2
                    SmartDashboard.putString("Target Position", "tx: " + Double.valueOf(tx) + " ty: " + Double.valueOf(ty) + " tz: " + Double.valueOf(tangle));
                    if (!check1)
                    {
                        if (!m_vision.camera.getLatestResult().hasTargets())
                        {
                            check1 = true;
                            check2 = true;
                            check3 = true;
                            check4 = true;
                            m_drive.setControl(forwardStraight.withVelocityY(0).withVelocityX(0).withRotationalRate(0));
                        }

                        if (ty < -Constants.Vision.Y_GOAL || ty > Constants.Vision.Y_GOAL)
                        {
                            if (ty < -Constants.Vision.Y_GOAL)
                            {
                                m_drive.setControl(forwardStraight.withVelocityY(Constants.Vision.speed));
                            }
                            if (ty > Constants.Vision.Y_GOAL)
                            {
                                m_drive.setControl(forwardStraight.withVelocityY(-Constants.Vision.speed));
                            }
                        }
                        else
                        {
                            m_drive.setControl(forwardStraight.withVelocityY(0));
                            check1 = true;
                        }
                    }
                    else if (!check2)
                    {
                        if (!m_vision.camera.getLatestResult().hasTargets())
                        {
                            check1 = true;
                            check2 = true;
                            check3 = true;
                            check4 = true;
                            m_drive.setControl(forwardStraight.withVelocityY(0).withVelocityX(0).withRotationalRate(0));
                        }

                        if (tangle < Constants.Vision.A_GOAL1 || tangle > Constants.Vision.A_GOAL2)
                        {
                            if (tangle < Constants.Vision.A_GOAL1)
                            {
                                m_drive.setControl(forwardStraight.withRotationalRate(Constants.Vision.speed));
                            }
                            if (tangle > Constants.Vision.A_GOAL2)
                            {
                                m_drive.setControl(forwardStraight.withRotationalRate(-Constants.Vision.speed));
                            }
                        }
                        else
                        {
                            m_drive.setControl(forwardStraight.withRotationalRate(0));
                            check2 = true;
                        }
                    }
                    else if (!check3)
                    {
                        if (!m_vision.camera.getLatestResult().hasTargets())
                        {
                            check1 = true;
                            check2 = true;
                            check3 = true;
                            check4 = true;
                            m_drive.setControl(forwardStraight.withVelocityY(0).withVelocityX(0).withRotationalRate(0));
                        }

                        if (ty < -Constants.Vision.Y_GOAL || ty > Constants.Vision.Y_GOAL)
                        {
                            if (ty < -Constants.Vision.Y_GOAL)
                            {
                                m_drive.setControl(forwardStraight.withVelocityY(Constants.Vision.speed));
                            }
                            if (ty > Constants.Vision.Y_GOAL)
                            {
                                m_drive.setControl(forwardStraight.withVelocityY(-Constants.Vision.speed));
                            }
                        }
                        else
                        {
                            m_drive.setControl(forwardStraight.withVelocityY(0));
                            check3 = true;
                        }
                    }
                    else if (!check4)
                    {
                        if (!m_vision.camera.getLatestResult().hasTargets())
                        {
                            check1 = true;
                            check2 = true;
                            check3 = true;
                            check4 = true;
                            m_drive.setControl(forwardStraight.withVelocityY(0).withVelocityX(0).withRotationalRate(0));
                        }
                        double tx_rounded = tx;
                        tx_rounded = 0.1 * Math.floor(tx_rounded * 10.0);
                        SmartDashboard.putNumber("Test", tx_rounded);
                        if (tx_rounded < (m_x - 0.2) || tx_rounded > (m_x + 0.2))
                        {
                            if (tx > m_x)
                            {
                                m_drive.setControl(forwardStraight.withVelocityX(-Constants.Vision.speed));
                            }
                            if (tx < m_x)
                            {
                                m_drive.setControl(forwardStraight.withVelocityX(Constants.Vision.speed));
                            }
                        }
                        else
                        {
                            m_drive.setControl(forwardStraight.withVelocityX(0));
                            check4 = true;
                        }
                    }
                    else
                    {
                        m_drive.setControl(forwardStraight.withVelocityY(0).withVelocityX(0).withRotationalRate(0));
                    }
                }
            }
        }
    }

    @Override
    public void end(boolean interrupted) 
    {
        m_drive.setControl(forwardStraight.withVelocityY(0).withRotationalRate(0));
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public boolean runsWhenDisabled() { return false; }
}