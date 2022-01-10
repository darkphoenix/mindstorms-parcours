package edu.kit.h2t.mindstorms.group2.segments;

import java.util.ArrayList;

import edu.kit.h2t.mindstorms.group2.ParcoursMain;
import edu.kit.h2t.mindstorms.group2.RobotUtil;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Bridge implements ParcoursSegment {
	private int state = 0;
	private int turns = 0;
	
	private final int sensorStopL = 75;
	private final int sensorStopR = 90;
	
	private final double diffEps = 0.1;
	private final double sumBlueEps = 0.3;
	
	private final int ArraySize = 100;
	
	
	private ArrayList<Float> LeftSamples;
	private ArrayList<Float> RightSamples;
	
	public void init() {
		LCD.clear();
		
		LeftSamples = new ArrayList<Float>(ArraySize * 2);
		RightSamples = new ArrayList<Float>(ArraySize);
		
//		sensorMover.rotateTo(sensorStopL);
		Sound.buzz();
		
	}

	public void doStep() {
		LCD.drawString("Value: " + RobotUtil.getRed(), 2, 3);
		LCD.drawString("Void: " + isVoid(), 2, 4);
		LCD.drawString("Angle: " + RobotUtil.getAngle(), 2, 5);
		LCD.drawString("State: " + state, 2, 6);
		
		switch(state) {
		
		//Get to Bridge
		case 0:
			getToBridge();
			state++;
			break;
		
		//Drive up	
		case 1:
			RobotUtil.syncForward();
			if(RobotUtil.getAngle() < 5) {
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
			if(RobotUtil.getAngle() < -5) {
				RobotUtil.syncForward();
			} else {
				state++;
			}
			break;
		default:
			RobotUtil.syncStop();
			break;
		
		}
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
	
	public void getToBridge() {
		while(RobotUtil.getAngle() < 5) {
			RobotUtil.syncForward();
		}
	}
	
	public boolean isVoid() {
		return RobotUtil.getRed() < 0.02;
	}
}
