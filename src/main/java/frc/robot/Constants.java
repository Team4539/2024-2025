package frc.robot;

public class Constants 
{
    public static final double stickDeadband = 0.1;
    public static final int blueTarget = 4;
    public static final int redTarget = 7;
    public static final class Arm
    {
        public static final int armID = 17;
        public static final int armInvertedID = 18;
        public static final double armMin = -41.5;
        public static final double armMax = 6; 
    }

    public static final class Intake
    {
        public static final int ID = 14;
        public static final double Speed = 0.75;
    }

    public static final class Climber
    {
        public static final int ID = 20;
        //public static final double Speed = .75;
        public static final double climberMin = -150;
        public static final double climberMax = 112;
    }
    public static final class Shooter
    {
        public static final int ID = 15;
        public static final int InvertedID = 16;
        public static final double shooterSpeed = 1;
    }
    public static final class Aiming 
    {
        public static final double Home = -43;
        public static final double Amp = -2.5;
        public static final double Source = 2;
        public static final double Position = -14;
    }
}
