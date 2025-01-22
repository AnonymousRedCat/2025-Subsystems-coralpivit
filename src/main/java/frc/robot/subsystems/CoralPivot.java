// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkMax;
import com.revrobotics.RelativeEncoder;
//import com.revrobotics.SparkPIDController;
//import com.revrobotics.SparkBase.ControlType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


//Some of this code is 2024 code, and may not be relevant
//if you see a 'tilt' varible, it is old code
//if you see a 'coral' variable, it is new code
//TODO make tilt varibles into coral varibles

public class CoralPivot extends SubsystemBase {
    private SpasrkMaxConfig coralPivotConfig;
  private SparkMax coralPivotMotor;
  private RelativeEncoder coralPivotEncoder;
  //this is copied from the 2024 code, this may not be relevant
  //SWITCHED TO WPILib PID, not SPARKMAX PID
  //private SparkPIDController tiltPIDController;
  private boolean isCoralPivotExtException, isCoralPivotRetException;
  private DigitalInput CoralExtLimit, CoralRetLimit;


    /** Creates a new CoralPivot. */
    public CoralPivot() {
    coralPivotMotor = new SparkMax(Constants.MotorControllers.ID_CARTRIDGE_TILT, MotorType.kBrushless);

    coralPivotMotor.restoreFactoryDefaults();
    coralPivotMotor.setSmartCurrentLimit(Constants.MotorControllers.SMART_CURRENT_LIMIT);
    coralPivotMotor.setInverted(false);//WAS TRUE - NOW USE NEGATIVE ENC VALUES TO TILT
    coralPivotEncoder = coralPivotMotor.getEncoder();
   // tiltPIDController = tiltMotor.getPIDController();
    }

    try {
      CoralExtLimit = new DigitalInput(Constants.coralPivot.DIO_CORAL_PIVOT_EXT_LIMIT);
    } catch (Exception e) {
       isCoralPivotExtException = true;
      SmartDashboard.putBoolean("exception thrown for Coral top limit: ", isCoralPivotExtException);
    }
    try {
      CoralRetLimit = new DigitalInput(Constants.coralPivot.DIO_CORAL_PIVOT_RET_LIMIT);
    } catch (Exception e) {
      isCoralPivotRetException = true;
      SmartDashboard.putBoolean("exception thrown for Coral bottom limit: ", isCoralPivotRetException);
    }
  } 

// do methods tomorrow