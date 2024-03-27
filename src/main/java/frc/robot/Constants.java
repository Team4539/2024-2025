package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Constants 
{
    public static final double stickDeadband = 0.1;
    public static final int blueTarget = 4;
    public static final int redTarget = 7;
    public static final class Arm
    {
        public static final int armID = 17;
        public static final int armInvertedID = 18;
        public static final int armEncoder = 0; //DIO port
        public static final double armMin = 0.10;
        public static final double armMax = 0.322;
        public static final int HEAD_ID = 7;
        public static final int headEncoder = 2; //DIO port
        public static final double headMin = 0.285;
        public static final double headMax = 0.47;
        public static final double headOffset = 0;
        public static final double armOffset = 0;
    }

    public static final class Intake
    {
        public static final int ID = 14;
        public static final int INDEXER_ID = 8;
        public static final double Speed = -1;
    }

    public static final class Climber
    {
        public static final int ID = 20;
        public static final double climberMin = -1000;
        public static final double climberMax = 1000;
        public static final double climberUp = 1;
        public static final double climberDown = -1;
    }

    public static final class Shooter
    {
        public static final int ID = 15;
        public static final int InvertedID = 16;
        public static final double shooterSpeed = 1;
    }

    public static final class Aiming 
    {
        public static final double Home = Arm.armMax;
        public static final double Home2 = Arm.headMin;
        public static final double Amp = 0.12; // for arm
        public static final double Amp2 = 0.45; // for head
        public static final double Source = .20; // for arm
        public static final double Upclose = .43; // for head
        
        public static int getTag()
        {
            Optional<Alliance> alliance = DriverStation.getAlliance();
            if (alliance.isPresent()) 
            {
                if (alliance.get() == DriverStation.Alliance.Blue)
                {
                    return 7;
                }
                else
                {
                    return 4;
                }
            }
            else
            {
                return 7;
            }
        }
    }

    public static final class arduinoCOMs
    {
        public static final int arduinoSend = 0; // PWM port
        public static final int arduinoRcv = 0; //analog
        public static final double Idle = 0.0; // pulse length is less than 15000us
        public static final double Auto = 1.0; // pulse length is less than greater than 15000us   
        public static final double Game = -1.0;  //  pulse length is less the 5000us
    }
}
