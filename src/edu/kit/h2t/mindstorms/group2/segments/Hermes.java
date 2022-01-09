package edu.kit.h2t.mindstorms.group2.segments;

import edu.kit.h2t.mindstorms.group2.ParcoursMain;
import edu.kit.h2t.mindstorms.group2.RobotUtil;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Hermes extends ParcoursSegment {

	private int leftTacho;
	private int rightTacho;
	
	public void init() {
		RobotUtil.rightMotor.setSpeed(1000);
		RobotUtil.leftMotor.setSpeed(1000);
		RobotUtil.leftMotor.rotate(4800, true);
		RobotUtil.rightMotor.rotate(4800, false);

		RobotUtil.leftMotor.rotate(-20, true);
		RobotUtil.rightMotor.rotate(20, false);
		
		RobotUtil.leftMotor.resetTachoCount();
		RobotUtil.rightMotor.resetTachoCount();
		
		leftTacho = RobotUtil.leftMotor.getTachoCount();
		rightTacho = RobotUtil.rightMotor.getTachoCount();
		
	}
	public void doStep() {
		RobotUtil.rightMotor.backward();
		RobotUtil.leftMotor.forward();
		float dis = RobotUtil.getDistance();
		
		LCD.drawString("Distance: " + dis, 2, 4);
		
		if(dis < 0.4) {
			RobotUtil.rightMotor.stop(true);
			RobotUtil.leftMotor.stop();
			
			ParcoursMain.HERMES_LEFT_DELTA = (leftTacho - RobotUtil.leftMotor.getTachoCount());
			ParcoursMain.HERMES_RIGHT_DELTA = (rightTacho - RobotUtil.rightMotor.getTachoCount());
			
			//Stop after finding box
			Delay.msDelay(250);
			
			//Back off
			//RobotUtil.leftMotor.rotate(-300, true);
			//RobotUtil.rightMotor.rotate(-300, false);
			
			RobotUtil.rightMotor.rotate(-1200, true);
			RobotUtil.leftMotor.rotate(1200, false);
			
			RobotUtil.rightMotor.setSpeed(10000);
			RobotUtil.leftMotor.setSpeed(10000);
			RobotUtil.rightMotor.backward();
			RobotUtil.leftMotor.backward();
			
//			Delay.msDelay(3000);
			
			int abort = 0;
			
			while(true) {
				LCD.drawString("Touch " + abort, 2,5);
				if(RobotUtil.getTouch()) {
					if(abort >= 0)
						abort++;
					if(abort > 600) {
						break;
					}
				} else {
					break;
				}
			}

			RobotUtil.rightMotor.stop(true);
			RobotUtil.leftMotor.stop();
			
			//Back off
			RobotUtil.leftMotor.rotate(600, true);
			RobotUtil.rightMotor.rotate(600, false);
			
			ParcoursMain.moveTo("Bridge");
		}
	}
	

}
