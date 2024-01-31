package frc.robot.subsystems;

import frc.robot.SwerveModule;
import frc.robot.Constants;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Swerve extends SubsystemBase {
    public SwerveDriveOdometry swerveOdometry;
    public SwerveModule[] mSwerveMods;
    public Pigeon2 gyro;
    public TalonFX intake;
    public TalonFX shooter;
    public TalonFX shooterInverted;
    public TalonFX arm;
    public TalonFX armInverted;
    public TalonFX climb;
    public static ColorSensorV3 m_colorSensor;
    public static ColorMatch m_colorMatcher;
    public boolean m_override;
    private Field2d field = new Field2d();

    public Swerve() {
        gyro = new Pigeon2(Constants.Swerve.pigeonID);
        // gyro.getConfigurator().apply(new Pigeon2Configuration());

        mSwerveMods = new SwerveModule[] {
            new SwerveModule(0, Constants.Swerve.Mod0.constants),
            new SwerveModule(1, Constants.Swerve.Mod1.constants),
            new SwerveModule(2, Constants.Swerve.Mod2.constants),
            new SwerveModule(3, Constants.Swerve.Mod3.constants)
        };

        swerveOdometry = new SwerveDriveOdometry(Constants.Swerve.swerveKinematics, getGyroYaw(), getModulePositions());

        intake = new TalonFX(Constants.Swerve.intakeID);
        intake.setInverted(true);
        shooter = new TalonFX(Constants.Swerve.shooterID);
        shooterInverted = new TalonFX(Constants.Swerve.shooterInvertedID);
        shooterInverted.setInverted(true);
        arm = new TalonFX(Constants.Swerve.armID);
        armInverted = new TalonFX(Constants.Swerve.armInvertedID);
        climb = new TalonFX(Constants.Climb.climbID);

        // Configure AutoBuilder
        AutoBuilder.configureHolonomic(
            this::getPose, 
            this::resetPose, 
            this::getSpeeds, 
            this::driveRobotRelative, 
            Constants.pathFollowerConfig,
            () -> {
                // Boolean supplier that controls when the path will be mirrored for the red alliance
                // This will flip the path being followed to the red side of the field.
                // THE ORIGIN WILL REMAIN ON THE BLUE SIDE
  
                var alliance = DriverStation.getAlliance();
                if (alliance.isPresent()) {
                    return alliance.get() == DriverStation.Alliance.Red;
                }
                return false;
            },
            this
        );
        PathPlannerLogging.setLogActivePathCallback((poses) -> field.getObject("path").setPoses(poses));
        SmartDashboard.putData("Field", field);
    }

    public ChassisSpeeds getSpeeds() {
        return Constants.Swerve.swerveKinematics.toChassisSpeeds(getModuleStates());
      }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.swerveKinematics.toSwerveModuleStates(
                fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation, 
                                    getHeading()
                                )
                                : new ChassisSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation)
                                );
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
        }
    }

public void driveRobotRelative(ChassisSpeeds robotRelativeSpeeds) 
{
    double x = robotRelativeSpeeds.vxMetersPerSecond;
    double y = robotRelativeSpeeds.vyMetersPerSecond;
    double rotation = -robotRelativeSpeeds.omegaRadiansPerSecond;
    drive(new Translation2d(x, y), rotation, false, false);
}
     /*public boolean isOrange()
    {
        if (m_override){return false;}
        Color detectedColor = m_colorSensor.getColor();
         ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        if (match.color == Constants.Swerve.kOrange)
        {
            //String RGB = "(" + detectedColor.red + ", " + detectedColor.green + ", " + detectedColor.blue + ")";
            //SmartDashboard.putString("RGB", RGB);
            return true;
        }
        else
        {
            return false;
        }
    }*/

    public void setShooter(double speed)
    {
        shooter.set(speed);
        shooterInverted.set(speed);
        
    }

    public void setIntake(double speed)
    {
        intake.set(speed);
    }

    public void setArm(double speed)
    {
        arm.set(speed);
        armInverted.set(speed);
    }


    /* Used by SwerveControllerCommand in Auto */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);
        
        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }

    public SwerveModuleState[] getModuleStates(){
        SwerveModuleState[] states = new SwerveModuleState[4];
        for(SwerveModule mod : mSwerveMods){
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModulePosition[] getModulePositions(){
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for(SwerveModule mod : mSwerveMods){
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    public Pose2d getPose() {
        return swerveOdometry.getPoseMeters();
    }

    public void resetPose(Pose2d pose) {
        swerveOdometry.resetPosition(getGyroYaw(), getModulePositions(), pose);
    }

    public Rotation2d getHeading(){
        return getPose().getRotation();
    }

    public void setHeading(Rotation2d heading){
        swerveOdometry.resetPosition(getGyroYaw(), getModulePositions(), new Pose2d(getPose().getTranslation(), heading));
    }

    public void zeroHeading(){
        swerveOdometry.resetPosition(getGyroYaw(), getModulePositions(), new Pose2d(getPose().getTranslation(), new Rotation2d()));
    }

    public Rotation2d getGyroYaw() {
        return Rotation2d.fromDegrees(gyro.getYaw().getValue()); //this inverts gyro
    }

    public void resetModulesToAbsolute(){
        for(SwerveModule mod : mSwerveMods){
            mod.resetToAbsolute();
        }
    }
    
    @Override
    public void periodic(){
        swerveOdometry.update(getGyroYaw(), getModulePositions());
        field.setRobotPose(getPose());
        /*for(SwerveModule mod : mSwerveMods){
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " CANcoder", mod.getCANcoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Angle", mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);    
        }*/
    }
}