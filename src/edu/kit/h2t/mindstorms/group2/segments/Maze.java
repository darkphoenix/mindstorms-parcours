package edu.kit.h2t.mindstorms.group2.segments;

import edu.kit.h2t.mindstorms.group2.ParcoursMain;
import edu.kit.h2t.mindstorms.group2.RobotUtil;
import lejos.hardware.Sound;

public class Maze implements ParcoursSegment {

	private boolean foundWhite = false;
	private boolean foundRed = false;
	private boolean turnDirectionLeft = true;
	
	private float whiteEps = 0.4f;
	private float redEps = 0.2f;
	
	public void init() {
	}

	public void doStep() {
		driveRoutine();
		searchRoutine();
		if(foundRed && foundWhite) {
			Sound.twoBeeps();
			RobotUtil.syncStop();
			ParcoursMain.moveTo(null);
		}
	}
	
	private void searchRoutine() {
		if(redEps < RobotUtil.getRed() && RobotUtil.getRed() < whiteEps)  {
			//Beep on first sound
			if(!foundRed) {
				Sound.beep();
			}
			foundRed = true;
		} 
		
		if(RobotUtil.getRed() > whiteEps) {
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
				RobotUtil.spin(-600);
			} else {
				//Turn right
				RobotUtil.spin(600);
			}			
			
		}
		
		while(!RobotUtil.getTouch()) {
			RobotUtil.syncBackward();
		}
		
		RobotUtil.syncForward();
	}
	
}
