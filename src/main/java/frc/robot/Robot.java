// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot 
{
  private Command m_autonomousCommand;
  private TalonFX calibrate_wheel;
  private RobotContainer m_robotContainer;
  // private LedCommSubsystem m_led;
  private double init_click;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    PortForwarder.add(5800, "photonvision.local", 5800);
    PortForwarder.add(5801, "photonvision-tag.local", 5801);
  }
  @Override
  public void robotPeriodic() 
  {
    CommandScheduler.getInstance().run(); 
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() 
  {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() 
  {
  }

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() 
  {
    CommandScheduler.getInstance().cancelAll();
    calibrate_wheel = new TalonFX(3);
    init_click = calibrate_wheel.getPosition().getValueAsDouble();
  }

  @Override
  public void testPeriodic() 
  {
  }

  @Override
  public void testExit() 
  {
    double clicks = calibrate_wheel.getPosition().getValueAsDouble();
    double cycles = clicks - init_click;
    double circumference = 36 / cycles;
    double diameter = (circumference / Math.PI) * 6.75;
    SmartDashboard.putNumber("kWheelRadiusInches", diameter / 2);
  }

  @Override
  public void simulationPeriodic() {}
}
