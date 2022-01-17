package edu.kit.h2t.mindstorms.group2.segments;

import java.util.ArrayList;

import edu.kit.h2t.mindstorms.group2.ParcoursMain;
import edu.kit.h2t.mindstorms.group2.RobotUtil;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class KoopLineFollow implements ParcoursSegment {
	private final double p = 1000;
	private final double offset = 0.3;

	private int direction = 1;
	
	private final int baseRegulateSpeed = 250;
	private final int baseSpeed = 360;
	private final float blackEps = 0.1f;
	private double whiteEps = 0.4f;
	private final double sumWhiteEps = 0.3;
	private final double diffEps = 0.08;
	
	private int ArraySize = 100;
	
	private ArrayList<Float> LeftSamples;
	private ArrayList<Float> RightSamples;
	
	public void init() {
		RobotUtil.setMotorSpeed(baseSpeed);
		LeftSamples = new ArrayList<Float>(ArraySize * 2);
		RightSamples = new ArrayList<Float>(ArraySize);
		calibrateWhite();
	}

	public void doStep() {
		LCD.drawString("V:" + RobotUtil.getDistance(),2,2);
		LCD.drawString("W:" + whiteEps,2,3);
		
		if (RobotUtil.getDistance() < 0.05) {
			RobotUtil.syncStop();
			RobotUtil.setMotorSpeed(baseSpeed);
			//Back off
			RobotUtil.leftMotor.rotate(-400, true);
			RobotUtil.rightMotor.rotate(-400, false);
			//Turn left
			RobotUtil.rightMotor.rotate(500, true);
			RobotUtil.leftMotor.rotate(-500, false);
			//Move
			RobotUtil.leftMotor.rotate(800, true);
			RobotUtil.rightMotor.rotate(800, false);
			//Turn right
			RobotUtil.rightMotor.rotate(-440, true);
			RobotUtil.leftMotor.rotate(440, false);
			//Forward
			RobotUtil.leftMotor.rotate(1400, true);
			RobotUtil.rightMotor.rotate(1400, false);
			//Turn right
			RobotUtil.rightMotor.rotate(-450, true);
			RobotUtil.leftMotor.rotate(450, false);
			//Move
			RobotUtil.leftMotor.rotate(600, true);
			RobotUtil.rightMotor.rotate(600, false);
			//Turn left
			RobotUtil.rightMotor.rotate(450, true);
			RobotUtil.leftMotor.rotate(-450, false);
			ParcoursMain.moveTo("Hermes");
		} else if (RobotUtil.getRed() > blackEps) {
			//LCD.drawString("regulated",4,6);
			regulatedLineTask();
			//LCD.clear(6);
		} else {
			RobotUtil.syncStop();
			//LCD.drawString("search",4,6);
			searchLine();
			//LCD.clear(6);
		}
	}
	
	public void searchLine() {
		while(true) {
			//LCD.drawString("rotate",4,6);
			rotateSensorTask();
			//LCD.clear(6);
			readSensorTask();
				
				//Drive into line direction until found.
			
			if(checkTachoTask()) {
				int sensorDirection = getDirection();
				
				RobotUtil.sensorMoverCenter();
				
				//Lücke
				if (sensorDirection == 0){
					RobotUtil.setMotorSpeed(baseSpeed);
					
					RobotUtil.leftMotor.rotate(100, true);
					RobotUtil.rightMotor.rotate(100, false);
				}
				//Korrektur
				else {
					while(RobotUtil.getRed() < whiteEps) {
						LCD.drawString("K:" + RobotUtil.getRed(),2,4);
						//Links
						if(sensorDirection == 1) {
							RobotUtil.rightMotor.rotate(100, true);
						} 
						//rechts
						else if(sensorDirection == -1) {
							RobotUtil.leftMotor.rotate(150, true);
							RobotUtil.rightMotor.rotate(-100, true);
						}
					}
					RobotUtil.syncStop();
				}	
				
				break;
			}
			
		} 
		
		RobotUtil.sensorMoverCenter();
		direction = 1;
		
		
					
	}
	
	public int getDirection() {
		double leftAvg = calculateAverage(LeftSamples);
		double rightAvg = calculateAverage(RightSamples);
		
		double diffAvg = leftAvg - rightAvg;
		
		
		LeftSamples = new ArrayList<Float>(ArraySize * 2);
		RightSamples = new ArrayList<Float>(ArraySize);
		
		LCD.clear(6);
		if(Double.toString(leftAvg).length() >= 4 && Double.toString(rightAvg).length() >= 4) {
		LCD.drawString("L:" + Double.toString(leftAvg).substring(1, 4) + "R:" + Double.toString(rightAvg).substring(1, 4) ,4,6);
		}
		
		LCD.clear(5);
		LCD.drawString("DiffAvg: " + diffAvg, 2,5);
		
		if(diffAvg > diffEps) {
			return 1;
		} else if (diffAvg < -diffEps || ((leftAvg+rightAvg) > sumWhiteEps)) {
			return -1;
		} else {
			return 0;
		}
		
	}
	
	private double calculateAverage(ArrayList <Float> marks) {
		  Float sum = 0f;
		  if(!marks.isEmpty()) {
		    for (Float mark : marks) {
		        sum += mark;
		    }
		    return sum.doubleValue() / marks.size();
		  }
		  return sum;
		}
	
	public void regulatedLineTask() {
		int y = (int) ((RobotUtil.getRed() - offset) * p);
		
		RobotUtil.leftMotor.setSpeed(baseRegulateSpeed + y);
		RobotUtil.rightMotor.setSpeed(baseRegulateSpeed - y);
		RobotUtil.leftMotor.forward();
		RobotUtil.rightMotor.forward();
	}
	
	public void rotateSensorTask() {
		if(direction == 1) {
			RobotUtil.sensorMoverLeft();
		} else {
			RobotUtil.sensorMoverRight();
		}
	}
	
	public int readSensorTask() {
		
		float currentValue = RobotUtil.getRed();
		if(RobotUtil.sensorMover.getTachoCount() > 0) {
			LeftSamples.add(currentValue);
		} else {
			RightSamples.add(currentValue);
		}
		
		
//		if(currentValue > blackEps) {
//			return direction;
//		}
		return 0;
	}
	
	
	public boolean checkTachoTask() {
		if(RobotUtil.isSensorMoverLeft()) {
			direction = -1;
			return false;
		} else if(RobotUtil.isSensorMoverRight()) {
			return true;
		}
		return false;
	}
	
	
	
	public float calibrateWhite() {
		ArrayList<Float>MidSamples = new ArrayList<Float>(ArraySize);
		
		int offset_angle = 15;
		RobotUtil.sensorMover.rotateTo(offset_angle);
		RobotUtil.sensorMoverLeft();
		while(!RobotUtil.isSensorMoverLeft()) {
			readSensorTask();
		}
		RobotUtil.sensorMover.rotateTo(offset_angle);
		RobotUtil.sensorMover.rotateTo(-offset_angle, true);
		while(RobotUtil.sensorMover.getTachoCount() > -(offset_angle)) {
			float currentValue = RobotUtil.getRed();
			MidSamples.add(currentValue);
		}
		
		RobotUtil.sensorMoverRight();
		while(!RobotUtil.isSensorMoverRight()) {
			readSensorTask();
		}
		
		RobotUtil.sensorMoverCenter();
		
		
		
		double leftAvg = calculateAverage(LeftSamples);
		double rightAvg = calculateAverage(RightSamples);
		double midAvg = calculateAverage(MidSamples);
		
		whiteEps = midAvg;
		
		LCD.clear(7);
		if(Double.toString(leftAvg).length() >= 4 && Double.toString(rightAvg).length() >= 4 && Double.toString(midAvg).length() >= 3) {
		LCD.drawString("L:" + Double.toString(leftAvg).substring(1, 4) + "M:" + Double.toString(midAvg).substring(1, 3) + "R:" + Double.toString(rightAvg).substring(1, 4) ,4,7);
		}
		
		LeftSamples = new ArrayList<Float>(ArraySize * 2);
		RightSamples = new ArrayList<Float>(ArraySize);
		MidSamples = new ArrayList<Float>(ArraySize);
		return 0.4f;
	}
}
