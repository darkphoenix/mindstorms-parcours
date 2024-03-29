package edu.kit.h2t.mindstorms.group2.segments;

import edu.kit.h2t.mindstorms.group2.ParcoursMain;
import edu.kit.h2t.mindstorms.group2.RobotUtil;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Bridge implements ParcoursSegment {
	private int state = 0;
	private int startState = 0;
	private int turns = 0;
	
	private final int baseRegulateSpeed = 250;
	private final double offset = 0.02;
	private final double p = 3000;
	private final double p2 = 1000;
	
	private boolean VoidAlligned = false;
	
	public Bridge(int state) {
		VoidAlligned = false;
		startState = state;
	}
	
	public Bridge() {
		
	}
	
	public void init() {
		state = startState;
		LCD.clear();
		RobotUtil.resetAngle();
		
//		sensorMover.rotateTo(sensorStopL);
//		Sound.buzz();
		
	}

	public void doStep() {
		
		LCD.drawString("Speed: " + RobotUtil.leftMotor.getSpeed(), 2, 2);
		LCD.drawString("Value: " + RobotUtil.getRed(), 2, 3);
		LCD.drawString("Void: " + isVoid(), 2, 4);
		LCD.drawString("Angle: " + RobotUtil.getAngle(), 2, 5);
		LCD.drawString("State: " + state, 2, 6);
		
		
//		unregulatedStates();
		regulatedStates();
		
//		
//		//Drive up 
//		if (getAngle() > 5) {
//			syncForward();
//		} 
//		//Drive down
//		else if (getAngle() < -5 ) {
//			sensorMover.rotateTo(-sensorStopR);
//			syncForward();
//		} 
//		else if(isVoid()) {
//			//Backoff
//			ParcoursMain.rightMotor.rotate(-100, true);
//			ParcoursMain.leftMotor.rotate(-100, false);
//			
//			//Turn left
//			ParcoursMain.rightMotor.rotate(600, true);
//			ParcoursMain.leftMotor.rotate(-600, false);
//		}
//		else {
//			syncStop();
//		}
		
	}
	
	private void regulatedStates() {
		switch(state) {
		
		//Get to Bridge
		case 0:
			getToBridge();
			state++;
			break;
		
		//Drive up	
		case 1:
			RobotUtil.sensorMoverLeft();
			regulatedDriveTask();
			if(RobotUtil.getAngle() < 5) {
				RobotUtil.setMotorSpeed(RobotUtil.baseSpeed);
				RobotUtil.sensorMoverCenter();
				while(!isVoid() && RobotUtil.chk()) {
					RobotUtil.syncForward();
				}
				
				//Backoff
				RobotUtil.rightMotor.rotate(-100, true);
				RobotUtil.leftMotor.rotate(-100, false);
				
				RobotUtil.spin(-590);
				
				state++;
//				Sound.beep();
			}
			
			if(isVoid()) {
			}
			break;
		
		//Correction	
		case 2:				
			if(isVoid()) {
				RobotUtil.syncStop();
				
				//Backoff
				RobotUtil.rightMotor.rotate(-180, true);
				RobotUtil.leftMotor.rotate(-180, false);
				
				//Turn left
				RobotUtil.spin(-590);
				state++;
//				Sound.beep();
				
				while(RobotUtil.getAngle() >= -5 && RobotUtil.chk()) {
					RobotUtil.syncForward();
				}
				
			} else {
				RobotUtil.syncForward();
			}
			break;
		//Drive down	
		case 3:
			//RobotUtil.sensorMoverLeft();
			Delay.msDelay(400);
			if(RobotUtil.getAngle() < -5) {
				RobotUtil.syncForward();
			} else {
				RobotUtil.setMotorSpeed(360, 300);
				//RobotUtil.sensorMoverRight();
				state++;
//				Sound.beep();
			}
			break;
		default:
			RobotUtil.setMotorSpeed(RobotUtil.baseSpeed);
			RobotUtil.syncStop();
			if (state == 4) {
				ParcoursMain.moveTo("Maze");
			}
			break;
		}	
		
	}
	
	private boolean allignVoid() {
		RobotUtil.rightMotor.resetTachoCount();
		RobotUtil.leftMotor.resetTachoCount();
		
		while(!isVoid() && RobotUtil.chk()) {
			int y = (int) ((RobotUtil.getRed() - offset) * p);
			
			RobotUtil.leftMotor.setSpeed(baseRegulateSpeed - y);
			RobotUtil.rightMotor.setSpeed(baseRegulateSpeed + y);
			
			if(RobotUtil.getAngle() > -5) {
				return false;
			}
		}
		RobotUtil.syncStop();
		
		while(isVoid() && RobotUtil.chk()) {
			RobotUtil.rightMotor.backward();
		}
		RobotUtil.rightMotor.stop();
		
//		RobotUtil.gameoverSound();
		
		//Backoff
		RobotUtil.rightMotor.rotate(-50, true);
		RobotUtil.leftMotor.rotate(-50, false);
		
		while(RobotUtil.getAbsTachoDiff() < 5 && RobotUtil.chk()) {
			RobotUtil.leftMotor.forward();
		}
		RobotUtil.leftMotor.stop();
		
		return true;
		
	}

	@Deprecated
	private void unregulatedStates() {
		switch(state) {
		
		//Get to Bridge
		case 0:
			getToBridge();
			state++;
			Sound.beep();
			break;
		
		//Drive up	
		case 1:
			RobotUtil.syncForward();
			RobotUtil.setMotorSpeed(600);
			if(RobotUtil.getAngle() < 5) {
				RobotUtil.setMotorSpeed(360);
				state++;
				Sound.beep();
			}
			break;
		
		//Correction	
		case 2:	
			
			if(isVoid() && turns < 2) {
				RobotUtil.syncStop();
				
				//Backoff
				RobotUtil.rightMotor.rotate(-100, true);
				RobotUtil.leftMotor.rotate(-100, false);
				
				//Turn left
				RobotUtil.spin(-600);
				
				turns++;
			} 
			else {
				RobotUtil.syncForward();
			}
			if(RobotUtil.getAngle() < -5) {
				state++;
				Sound.beep();
			}
			break;
		//Drive down	
		case 3:
			RobotUtil.setMotorSpeed(600);
			RobotUtil.leftMotor.setSpeed(400);
			if(RobotUtil.getAngle() < -5) {
				RobotUtil.syncForward();
				if (isVoid())
					Sound.beep();
			} else {
				RobotUtil.setMotorSpeed(360);
				
				state++;
				Sound.beep();
			}
			
				
			break;
		default:
			RobotUtil.setMotorSpeed(360);
			RobotUtil.syncStop();
			break;
		}	
		
	}
	
	private void regulatedDriveTask(boolean inverted) {
		
		if(!inverted) {
			int y = (int) ((RobotUtil.getRed() - offset) * p);
		
			RobotUtil.leftMotor.setSpeed(baseRegulateSpeed - y);
			RobotUtil.rightMotor.setSpeed(baseRegulateSpeed + y);
		
			RobotUtil.syncForward();
		} 
		else {
			int y = (int) (-1 * (RobotUtil.getRed() - offset) * p2);
			
			RobotUtil.leftMotor.setSpeed(baseRegulateSpeed + y);
			RobotUtil.rightMotor.setSpeed(baseRegulateSpeed - y);
			
			RobotUtil.syncBackward();
		}
		
	}
	
	private void regulatedDriveTask() {
		regulatedDriveTask(false);
	}
	
	public void getToBridge() {
		while(RobotUtil.getAngle() < 5 && RobotUtil.chk()) {
			RobotUtil.syncForward();
		}
	}
	
	public boolean isVoid() {
		return RobotUtil.getRed() < 0.02;
	}
}
