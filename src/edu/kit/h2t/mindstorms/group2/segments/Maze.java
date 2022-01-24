package edu.kit.h2t.mindstorms.group2.segments;

import edu.kit.h2t.mindstorms.group2.ParcoursMain;
import edu.kit.h2t.mindstorms.group2.RobotUtil;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class Maze implements ParcoursSegment {

	private boolean foundWhite = false;
	private boolean foundRed = false;
	private boolean turnDirectionLeft = true;
	
	private float whiteEps = 0.45f;
	private float redEps = 0.2f;
	
	public void init() {
		while(RobotUtil.getDistance() > 0.2) {
			RobotUtil.syncForward();
		}
		RobotUtil.turn(1200, false);
	}

	public void doStep() {
		LCD.drawString("White: " + foundWhite, 3, 4);
		LCD.drawString("Red: " + foundRed, 3, 5);
		
		driveRoutine();
		searchRoutine();
		if(foundRed && foundWhite) {
			Sound.twoBeeps();
			RobotUtil.syncStop();
			ParcoursMain.moveTo(null);
		}
	}
	
	private void searchRoutine() {
		float color = RobotUtil.getRed();
		if(redEps < color && color < whiteEps)  {
			//Beep on first sound
			if(!foundRed) {
				Sound.beep();
			}
			foundRed = true;
		} 
		
		if(color > whiteEps) {
			//Beep on first found
			if (!foundWhite) {
				Sound.beep();
			}
			foundWhite = true;
		}
	}

	private boolean isWhite() {
		return false;
	}
	
	private boolean isRed() {
		return false;
	}
	
	private void driveRoutine() {
		if(RobotUtil.getDistance() < 0.1) {
			//Backoff
			RobotUtil.rightMotor.rotate(-100, true);
			RobotUtil.leftMotor.rotate(-100, false);
			
			if(turnDirectionLeft) {
				//Turn left
				RobotUtil.turn(2550, false);
			} else {
				//Turn right
				RobotUtil.turn(2550, true);
			}
		
			turnDirectionLeft = !turnDirectionLeft;
			
			while(!RobotUtil.getTouch()) {
				RobotUtil.syncBackward();
			}
			
		}
		
		RobotUtil.syncForward();
	}
	
}
