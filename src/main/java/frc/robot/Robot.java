// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SlewRateLimiter;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandBase;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;

import frc.robot.commands.*;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs
 * the motors with arcade steering.
 */
public class Robot extends TimedRobot {

  public static VictorSPX m_frontLeft = new VictorSPX(4);
  public static VictorSPX m_rearLeft = new VictorSPX(1);

  public static VictorSPX m_frontRight = new VictorSPX(3);
  public static VictorSPX m_rearRight = new VictorSPX(2);

  public static Joystick xBox = new Joystick(0);

  SlewRateLimiter filter;

  CommandBase autonomousCommand;
    
    double turnPercent, forwardPercent;

     public void teleopInit() {
       filter = new SlewRateLimiter(getSlewRateLimiter());
       System.out.println ("SlewRateLimiter");
     }

    public void robotInit() {
      m_rearLeft.follow(m_frontLeft) ;
      m_rearRight.follow(m_frontRight) ;
       m_frontLeft.setInverted(true); // if you want to invert the entire side you can do so here
       autonomousCommand = new DriveForwardGivenTime(0.3, 0.75);
    }
  private final Joystick joystick = new Joystick(1);

  public double getSlewRateLimiter()
  {
    double slewRateLimiter = (joystick.getRawAxis(3) + 1 ) / 2. ;
    return slewRateLimiter;
  }

  public double getAxis(int axis) 
  {
		 double val = xBox.getRawAxis(axis);
		//  if (Math.abs(val) <= DEADBAND_WIDTH) {
		//  	val = 0.0;
		//  }
     
     //filter = new SlewRateLimiter(0.5);
     filter.calculate(val);
     val = Math.pow(val, 3);
		return val;
	}
  
  public double leftYAxis(){
    return Math.pow(this.getAxis(1) * 1, 3);
  }

  public double leftXAxis(){
    return Math.pow(this.getAxis(0) * 1, 3);
  }

  public double rightYAxis(){
   return Math.pow(this.getAxis(5) * 1, 3);
  }

  public double rightXAxis(){
    return Math.pow(this.getAxis(4) * 1, 3);

  }

  @Override
  public void autonomousInit() {
    // schedule the autonomous command (example)
    if (autonomousCommand != null) 
      ((Object) autonomousCommand).start();
  }

  public void teleopPeriodic() {
       //set turnPercent and forwardPercent to axes on the controller
       turnPercent = -this.rightXAxis();
       forwardPercent = this.leftYAxis();

       //calculate leftSideSpeed and rightSideSpeed
       double leftSideSpeed = (forwardPercent + turnPercent );
       double rightSideSpeed = (forwardPercent - turnPercent );

       m_frontLeft.set (VictorSPXControlMode.PercentOutput, leftSideSpeed);
       //System.out.println(leftYAxis());
       m_frontRight.set (VictorSPXControlMode.PercentOutput, rightSideSpeed);
       //System.out.println(rightYAxis());
   }
    // // Tank drive with a given left and right rates
    // myDrive.tankDrive(-leftStick.getY(), -rightStick.getY());

    // // Arcade drive with a given forward and turn rate
    // myDrive.arcadeDrive(-driveStick.getY(), driveStick.getX());

    // // Curvature drive with a given forward and turn rate, as well as a quick-turn button
    // myDrive.curvatureDrive(-driveStick.getY(), driveStick.getX(), driveStick.getButton(1));
}

