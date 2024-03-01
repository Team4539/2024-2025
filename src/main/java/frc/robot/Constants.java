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
        public static final int armEncoder = 0; //DIO port
        public static final double armMin = Aiming.Home;
        public static final double armMax = 24; 
    }

    public static final class Intake
    {
        public static final int ID = 14;
        public static final double Speed = 1;
    }

    public static final class Climber
    {
        public static final int ID = 20;
        //public static final double Speed = .75;
        public static final double climberMin = -180;
        public static final double climberMax = 94;
    }

    public static final class Shooter
    {
        public static final int ID = 15;
        public static final int InvertedID = 16;
        public static final double shooterSpeed = 1;
    }

    public static final class Aiming 
    {
        public static final double Home = -0.9;
        public static final double Amp = 12.25;
        public static final double Source = 15.4;
        public static final double Position = 3.9;
        public static final double Middle = 6.9;
        //public static final double Farback = 7.25;
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
