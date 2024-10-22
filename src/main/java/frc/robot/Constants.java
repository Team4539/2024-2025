package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Constants 
{
    public static final double stickDeadband = 0.1;
    public static final int blueTarget = 4;
    public static final int redTarget = 7;

    public static final class Trap
    { 
        public static final double hell = 0.10; // arm
        public static final double heaven = 0.2; // head
    }

    public static final class Servo
    {
        public static final int id = 0;
        public static final double position = 0.75;
        public static final double home = 1;
    }

    public static final class Arm
    {
        public static final int armID = 17;
        public static final int armInvertedID = 18;
        public static final int armEncoder = 0; //DIO port
        public static final double armMin = 0.10;
        public static final double armMax = 0.330;
        public static final int HEAD_ID = 24;
        public static final int headEncoder = 2; //DIO port
        public static final double headMin = 0.2;
        public static final double headMax = 0.47;
        public static final double headOffset = 0;
        public static final double armOffset = 0;
    }

    public static final class Intake
    {
        public static final int ID = 14;
        public static final int INDEXER_ID = 8;
        public static final double Speed = -1;
        public static final int limit_id = 3;
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
        public static final double Home2 = 0.28;
        public static final double Amp = 0.102; // for arm
        public static final double Amp2 = 0.45; // for head
        public static final double Source = 0.20; // for arm
        public static final double Upclose = 0.425; // for head
        public static final double kindaUpClose = 0.459;
        public static final double midUpClose = 0.442;
        public static final double safeHead = 0.290;
        public static final double safeArm = 0.11;
        public static final double lineArm = 0.0;
        public static final double lineHead = 0.459;
        public static final double pickupShootPosition = 0.473;
        public static final double farArm = 0.2758614;

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

    public static final class Vision
    {
        public static final double speed = 0.4;
        public static final double speed2 = 1.2;
        public static final double Y_GOAL = 0.05;
        public static final double A_GOAL1 = 179;
        public static final double A_GOAL2 = 181;
    }
}
