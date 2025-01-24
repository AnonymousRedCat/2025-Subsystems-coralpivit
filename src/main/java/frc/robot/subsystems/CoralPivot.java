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
// methods start here
public double getCoralEncoder() {  //gives encoder reading in Revs
  return coralEncoder.getPosition();
}

public void resetCoralEncoder() {
  coralEncoder.setPosition(0);
}

public void stopCoralPivot() {
coralPivotMotor.set(0);
}

public boolean isCoralExtLimit() {
if (isCoralPivotExtException) {
  return true;
} else {
  return CoralExtLimit.get();
}
}

public boolean isCoralRetLimit() {
if (isCoralPivotRetException) {
  return true;
} else {
  return CoralRetLimit.get();
}
}
public boolean isFullyExtended() {
  boolean aFullExtend;
  if (getCoralEncoder() <= Constants.coralPivot.CORAL_ENC_REVS_MAX) {  //WAS >=
    aFullExtend = true;
  } else {
    aFullExtend = false;      }
  return aFullExtend;
}
public void setCoralPivotSpeed(double speed) {
  if (speed <= 0) {  //was >0 but changed since going negative when extending now
     //TODO make sure elevator speed > 0 when going up
    if (isCoralExtLimit() || isFullyExtended()) {
        // if fully extended limit is tripped or cartridge at the maximum desired extension and going out, stop 
        stopCoralPivot();
     }  else {
        // cartridge extending out but fully extended limit is not tripped, go at commanded speed
       coralPivotMotor.set(speed);
      }
 } 
 else {
      if (isCoralRetLimit()) {
        // cartridge retracting and retract limit is tripped, stop and zero encoder
        stopCoralPivot();
        resetCoralEncoder();
      } else {
        // cartridge retracting but fully retracted limit is not tripped, go at commanded speed
        coralPivotMotor.set(speed);
      }
     }
}

public double getCoralTiltSpeed() {
  return coralPivotMotor.get();
}

//Begin things that may not be relevant
//these are things that might be useful in the future if we use CANSparkMax PID
//we are not currently using it

//!!!! SPARKMAX PID STUFF - USE SPARKMAX PID, NOT WPILib PID 
//**** CHANGED BACK TO USING WPILib PID ****
//**** due to spurious encoder polarity changes when run multiple autos in a row ****
/*
public void setSetpoint(double encoderRevs) {
  tiltPIDController.setReference(encoderRevs, ControlType.kPosition);
}

public void setP(double kP) {
  tiltPIDController.setP(kP);
}

public void setI(double kI) {
  tiltPIDController.setI(kI);
}

public void setD(double kD) {
  tiltPIDController.setD(kD);
}

public void setFF(double kFF) {
  tiltPIDController.setFF(kFF);
}
*/


@Override
  public void periodic() {
    SmartDashboard.putBoolean("Coral Pivot Extend Limit: ", isCoralExtLimit());
    SmartDashboard.putBoolean("Coral Pivot Retract Limit", isCoralRetLimit());
    SmartDashboard.putNumber("Coral Pivot Encoder Revolutions ", getCoralEncoder());
    SmartDashboard.putBoolean("Coral Pivot is fully extended: ", isFullyExtended());
  }

}