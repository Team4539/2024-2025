// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.LedCommSubsystem;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  private LedCommSubsystem m_led;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    m_led = m_robotContainer.m_led;
    PortForwarder.add(5800, "photonvision.local", 5800); // for photonvision
    m_led.setLed(Constants.arduinoCOMs.Idle);
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
    m_led.setLed(Constants.arduinoCOMs.Auto);

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {
    m_led.setLed(Constants.arduinoCOMs.Idle);

  }

  @Override
  public void teleopInit() 
  {
    m_led.setLed(Constants.arduinoCOMs.Game);
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() 
  {
  }

  @Override
  public void teleopExit() {
    m_led.setLed(Constants.arduinoCOMs.Idle);

  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  @Override
  public void simulationPeriodic() {}
}
