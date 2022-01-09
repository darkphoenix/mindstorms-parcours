package edu.kit.h2t.mindstorms.group2.segments;

import java.util.ArrayList;

import edu.kit.h2t.mindstorms.group2.ParcoursMain;
import edu.kit.h2t.mindstorms.group2.RobotUtil;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Bridge extends ParcoursSegment {
	private final int sensorStopL = 75;
	private final int sensorStopR = 90;
	
	private final double diffEps = 0.1;
	private final double sumBlueEps = 0.3;
	
	private final int ArraySize = 100;
	
	private ArrayList<Float> LeftSamples;
	private ArrayList<Float> RightSamples;
	
	private int leftDelta;
	private int rightDelta;
	
	private boolean blueFound = false;
	
	private final float coloreps = 0.05f;
	
	public void init() {
		
		LeftSamples = new ArrayList<Float>(ArraySize * 2);
		RightSamples = new ArrayList<Float>(ArraySize);
		
		leftDelta = ParcoursMain.HERMES_LEFT_DELTA;
		rightDelta = ParcoursMain.HERMES_RIGHT_DELTA;
		
		RobotUtil.leftMotor.resetTachoCount();
		RobotUtil.rightMotor.resetTachoCount();
		
		while((RobotUtil.leftMotor.getTachoCount() - leftDelta) > 5) {
			RobotUtil.rightMotor.forward();
			RobotUtil.leftMotor.backward();
		} 

		RobotUtil.rightMotor.stop(true);
		RobotUtil.leftMotor.stop();
		
		
		LCD.clear();
	}
	public void doStep() {
		float[] rgb = RobotUtil.getRGB();
		
		LCD.drawString("R_Delta: " + rightDelta, 2, 1);
		LCD.drawString("L_Delta: " + leftDelta, 2, 2);
		LCD.drawString("R_Tacho: " + RobotUtil.rightMotor.getTachoCount(), 2, 3);
		LCD.drawString("L_Tacho: " + RobotUtil.leftMotor.getTachoCount(), 2, 4);
		
		
//		LCD.drawString("red: " + rgb[0], 2, 3);
//		LCD.drawString("green: " + rgb[1], 2, 4);
//		LCD.drawString("blue: " + rgb[2], 2, 5);
//		
//		LCD.drawString("color:   " + getColor(), 2,6 );
		
		while(!blueFound) {
			
			LCD.drawString("Blue? :   " + isBlueLine() + "!", 2,6 );
			RobotUtil.rightMotor.forward();
			RobotUtil.leftMotor.forward();
			
			if(isBlueLine()) {
				blueFound = true;
				RobotUtil.rightMotor.stop(true);
				RobotUtil.leftMotor.stop();
				readBlue();
				double diff = getDirectionDiff();
				LCD.drawString("D: " + diff, 2, 7);
				allignBlue(diff);
				break;
			}	
		}
		
		
		Delay.msDelay(50);
	}
	
	public void readBlue() {
		if(RobotUtil.sensorMover != null) {
			RobotUtil.sensorMover.rotateTo(sensorStopL, true);
			while(RobotUtil.sensorMover.getTachoCount() < sensorStopL) {
				readSensorTask();
			}
			RobotUtil.sensorMover.rotateTo(-sensorStopR, true);
			while(RobotUtil.sensorMover.getTachoCount() > -(sensorStopR)) {
				readSensorTask();
			}
			RobotUtil.sensorMover.rotateTo(0);
		} else {
			System.out.println("NULL!");
		}
	}
		
	
	/*  Diff = leftAvg - rightAvg
	 * 	Left means positive diff
		Right means negative diff
	*/
	public void allignBlue(double diff) {
		int baseRotate = 360;
		RobotUtil.leftMotor.rotate((int) (baseRotate * -diff), true);
		RobotUtil.rightMotor.rotate((int) (baseRotate * diff), false);
	}
	
	/*
	 * return -1 as error value.
	 * return 0 for red,
	 * return 1 for green,
	 * return 2 for blue.
	 */
	public int getColor() {
		float[] rgb = RobotUtil.getRGB();
		float red = rgb[0];
		float green = rgb[1];
		float blue = rgb[2];
		
		if (red > coloreps && green <= coloreps && blue <= coloreps) {
			//red
			return 0;
		} else if (red <= coloreps && green > coloreps && blue <= coloreps) {
			//green
			return 1;
		} else if (red <= coloreps && green <= coloreps && blue > coloreps) {
			//blue
			return 2;				
		}
		
		//error
		return -1;
	}
	
	public void readSensorTask() {
		
		float currentValue = 0;
		
		if(isBlueLine()){
			currentValue = 1;
		}
		if(RobotUtil.sensorMover.getTachoCount() > 0) {
			LeftSamples.add(currentValue);
		} else {
			RightSamples.add(currentValue);
		}
	}	
	
	public boolean isBlueLine() {
		float[] rgb = RobotUtil.getRGB();
		float red = rgb[0];
		float green = rgb[1];
		float blue = rgb[2];
		
		if (red > coloreps) {
			return false;
		} else if (red <= coloreps && (green > coloreps || blue > coloreps)) {
			//green
			return true;			
		}
		
		return false;
	}

	public int getDirection() {
		double leftAvg = calculateAverage(LeftSamples);
		double rightAvg = calculateAverage(RightSamples);
		
		double diffAvg = Math.abs(getDirectionDiff());
		
		if(diffAvg < diffEps) {
			return 0;
		} else if (leftAvg > rightAvg) {
			return -1;
		} else {
			return 1;
		}
		
	}
	
	public double getDirectionDiff() {
		double leftAvg = calculateAverage(LeftSamples);
		double rightAvg = calculateAverage(RightSamples);
		
		double diffAvg = leftAvg - rightAvg;
		
		LeftSamples = new ArrayList<Float>(ArraySize * 2);
		RightSamples = new ArrayList<Float>(ArraySize);
		
		LCD.clear(6);
		if(Double.toString(leftAvg).length() >= 4 && Double.toString(rightAvg).length() >= 4) {
		LCD.drawString("L:" + Double.toString(leftAvg).substring(0, 4) + "R:" + Double.toString(rightAvg).substring(0, 4) ,4,6);
		} else if(Double.toString(leftAvg).length() >= 3 && Double.toString(rightAvg).length() >= 3) {
			LCD.drawString("L:" + Double.toString(leftAvg).substring(0, 3) + "R:" + Double.toString(rightAvg).substring(0, 3) ,4,6);
		} else {
			LCD.drawString("L:" + Double.toString(leftAvg) + "R:" + Double.toString(rightAvg) ,4,6);
		}
		 
		LCD.clear(5);
		LCD.drawString("DiffAvg: " + diffAvg, 2,5);
		
		return diffAvg;
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
}
