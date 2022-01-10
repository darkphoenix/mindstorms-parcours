package edu.kit.h2t.mindstorms.group2.segments;

import java.util.ArrayList;

import edu.kit.h2t.mindstorms.group2.ParcoursMain;
import edu.kit.h2t.mindstorms.group2.RobotUtil;
import lejos.hardware.Sound;
import lejos.utility.Delay;

public class FindBridge implements ParcoursSegment {
	private final double diffEps = 0.1;
	private final double sumBlueEps = 0.3;
	
	private final int ArraySize = 100;
	
	private ArrayList<Float> LeftSamples;
	private ArrayList<Float> RightSamples;
	
	private int leftDelta;
	private int rightDelta;
	
	private final float redeps = 0.05f;
	private final float blueeps = 0.08f;
	
	public void init() {
		
		LeftSamples = new ArrayList<Float>(ArraySize * 2);
		RightSamples = new ArrayList<Float>(ArraySize);
		
		
		RobotUtil.leftMotor.resetTachoCount();
		RobotUtil.rightMotor.resetTachoCount();
		
		RobotUtil.setMotorSpeed(200);
		RobotUtil.leftMotor.rotate(300, true);
		RobotUtil.rightMotor.rotate(300, false);
		
		RobotUtil.lcd.clear();
		
//		correctCourseTacho();
//		correctNaive();
//		correctCourseWhite();
		correctDistance();
		
		RobotUtil.setMotorSpeed(500);
	
	}
	public void doStep() {
		float[] rgb = RobotUtil.getRGB();
		float red = rgb[0];
		float green = rgb[1];
		float blue = rgb[2];
				
//		lcd.drawString("R_Delta: " + rightDelta, 2, 1);
//		lcd.drawString("L_Delta: " + leftDelta, 2, 2);
//		lcd.drawString("R_Tacho: " + ParcoursMain.rightMotor.getTachoCount(), 2, 3);
//		lcd.drawString("L_Tacho: " + ParcoursMain.leftMotor.getTachoCount(), 2, 4);
		
		
		RobotUtil.lcd.drawString("red: " + red, 2, 3);
		RobotUtil.lcd.drawString("green: " + green, 2, 4);
		RobotUtil.lcd.drawString("blue: " + blue, 2, 5);
//		
//		lcd.drawString("color:   " + getColor(), 2,6 );
		
		
			
		RobotUtil.lcd.drawString("Blue? :   " + isBlueLine() + "!", 2,6 );
		RobotUtil.rightMotor.forward();
		RobotUtil.leftMotor.forward();
		
		if(isBlueLine()) {
			RobotUtil.rightMotor.stop(true);
			RobotUtil.leftMotor.stop();
			readBlue();
			double diff = getDirectionDiff();
			RobotUtil.lcd.drawString("D: " + diff, 2, 7);
			alignBlue(diff);
			ParcoursMain.moveTo("Bridge");
		}	
	
		
		Delay.msDelay(50);
		
	}
	
	
	private void correctNaive() {
		RobotUtil.rightMotor.rotate(250, true);
		RobotUtil.leftMotor.rotate(-250, false);
	}
	
	private void correctCourseTacho() {
		while((RobotUtil.leftMotor.getTachoCount() - leftDelta) > 5) {
			RobotUtil.rightMotor.forward();
			RobotUtil.leftMotor.backward();
		} 

		RobotUtil.rightMotor.stop(true);
		RobotUtil.leftMotor.stop();
		
	}
	
	private void correctDistance() {
		RobotUtil.lcd.clear();
		while(RobotUtil.getDistance() < 0.9) {
			//Turn left
			RobotUtil.rightMotor.forward();
			RobotUtil.leftMotor.backward();
			
		}
		RobotUtil.lcd.drawString("D1: " + RobotUtil.getDistance(), 2, 6);
		Sound.beep();
		
		while(RobotUtil.getDistance() > 0.8) {
			//Turn left
			RobotUtil.rightMotor.forward();
			RobotUtil.leftMotor.backward();
			RobotUtil.lcd.drawString("D2: " + RobotUtil.getDistance(), 2, 7);
		}
		
		Sound.beep();
		
//		ParcoursMain.leftMotor.rotate(100, true);
//		ParcoursMain.rightMotor.rotate(-100, false);
		
		RobotUtil.rightMotor.stop(true);
		RobotUtil.leftMotor.stop();
	}
	
	private void correctCourseWhite() {
		while(true) {
			
			RobotUtil.lcd.drawString("White? :   " + isBlueLine() + "!", 2,6 );
			RobotUtil.rightMotor.forward();
			RobotUtil.leftMotor.forward();
			
			if(isBlueLine()) {
				RobotUtil.rightMotor.stop(true);
				RobotUtil.leftMotor.stop();
				readBlue();
				double diff = getDirectionDiff();
				RobotUtil.lcd.drawString("D: " + diff, 2, 7);
				alignBlue(diff);
				break;
			}	
		}
	}
	
	public void readBlue() {
		RobotUtil.sensorMoverLeft();
		while(!RobotUtil.isSensorMoverLeft()) {
			readSensorTask();
		}
		RobotUtil.sensorMoverRight();
		while(RobotUtil.isSensorMoverRight()) {
			readSensorTask();
		}
		RobotUtil.sensorMoverCenter();
	}
		
	
	/*  Diff = leftAvg - rightAvg
	 * 	Left means positive diff
	 *	Right means negative diff
	*/
	public void alignBlue(double diff) {
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
		
		if (red > redeps && green <= blueeps && blue <= blueeps) {
			//red
			return 0;
		} else if (red <= redeps && green > blueeps && blue <= blueeps) {
			//green
			return 1;
		} else if (red <= redeps && green <= blueeps && blue > blueeps) {
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
		if(RobotUtil.sensorMoverTacho() > 0) {
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
		
		if (red > redeps) {
			return false;
		} else if (red <= redeps && (green > blueeps || blue > blueeps)) {
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
		
		RobotUtil.lcd.clear(6);
		if(Double.toString(leftAvg).length() >= 4 && Double.toString(rightAvg).length() >= 4) {
			RobotUtil.lcd.drawString("L:" + Double.toString(leftAvg).substring(0, 4) + "R:" + Double.toString(rightAvg).substring(0, 4) ,4,6);
		} else if(Double.toString(leftAvg).length() >= 3 && Double.toString(rightAvg).length() >= 3) {
			RobotUtil.lcd.drawString("L:" + Double.toString(leftAvg).substring(0, 3) + "R:" + Double.toString(rightAvg).substring(0, 3) ,4,6);
		} else {
			RobotUtil.lcd.drawString("L:" + Double.toString(leftAvg) + "R:" + Double.toString(rightAvg) ,4,6);
		}
		 
		RobotUtil.lcd.clear(5);
		RobotUtil.lcd.drawString("DiffAvg: " + diffAvg, 2,5);
		
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
