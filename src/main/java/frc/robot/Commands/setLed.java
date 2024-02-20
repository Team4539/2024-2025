package frc.robot.Commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LedCommSubsystem;

public class setLed extends Command {
    private final LedCommSubsystem m_led;
    private final DoubleSupplier m_speed;

    public setLed(DoubleSupplier speed, LedCommSubsystem subsystem) {
        addRequirements(subsystem);
        m_led = subsystem;
        m_speed = speed;

    }
    
    public void execute() {
        
        m_led.setLed(m_speed.getAsDouble());
    }
}
