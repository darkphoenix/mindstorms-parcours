package edu.kit.h2t.mindstorms.group2;

import lejos.utility.Delay;

import java.util.ArrayList;

import lejos.ev3.tools.LCDDisplay;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;

public enum ParcoursSegment {
	/*LINEFOLLOW("Follow line") {
		private final double p = 1000;
		private final double offset = 0.3;
		
		private Port colorPort;
		private EV3ColorSensor color;
		private RegulatedMotor sensorMover;
		private SensorMode redMode;
		public void init() {
			colorPort = ParcoursMain.brick.getPort("S1");
			color = new EV3ColorSensor(colorPort);
			sensorMover = new EV3MediumRegulatedMotor(MotorPort.D);
			redMode = color.getRedMode();
			ParcoursMain.leftMotor.setSpeed(360);
			ParcoursMain.rightMotor.setSpeed(360);
			sensorMover.setSpeed(3600);
		}
		public void doStep() {
			//Drive on line
			float res[] = new float[1];
//			ParcoursMain.leftMotor.synchronizeWith(new RegulatedMotor[]{ParcoursMain.rightMotor});
//			ParcoursMain.rightMotor.synchronizeWith(new RegulatedMotor[]{ParcoursMain.leftMotor});
			
			while (getRedValue() > 0.1) {
				int y = (int) ((getRedValue() - offset) * p);
				LCD.drawInt(y, 4, 6);
				
				ParcoursMain.leftMotor.setSpeed(180 + y);
				ParcoursMain.rightMotor.setSpeed(180 - y);
				ParcoursMain.leftMotor.forward();
				ParcoursMain.rightMotor.forward();
				
				LCD.clear(6);
				LCD.drawString("DO  " + Float.toString(res[0]).substring(0, 3), 4, 6);
			}
			
			ParcoursMain.leftMotor.setSpeed(360);
			ParcoursMain.rightMotor.setSpeed(360);
			ParcoursMain.leftMotor.stop(true);
			ParcoursMain.rightMotor.stop();
			
			
			//Lost line, let's find it
			float[] leftSample = new float[1];
			float[] rightSample = new float[1];
			
			//Rotate to left
			sensorMover.rotateTo(-60, false);
			redMode.fetchSample(leftSample, 0);
			
			//Rotate to right
			sensorMover.rotateTo(60, false);
			redMode.fetchSample(rightSample, 0);
			
			//Compare Values of left and right
			float diff = leftSample[0] - rightSample[0];
			
			sensorMover.rotateTo(0, false);
			
			LCD.clear(6);
			LCD.drawString("LOST " + Float.toString(diff).substring(0, 3), 4, 6);
			
//			ParcoursMain.leftMotor.startSynchronization();
			float eps = 0.1f;
			
			//line is left
			if (diff < -eps) {
				LCD.clear(6);
				LCD.drawString("Links:  " + Float.toString(diff).substring(0, 3), 4, 6);
				ParcoursMain.leftMotor.stop(true);
				ParcoursMain.rightMotor.stop();
				sensorMover.rotateTo(0, false);
				
				do {
					redMode.fetchSample(res, 0);
				
//					ParcoursMain.leftMotor.startSynchronization();
					ParcoursMain.rightMotor.rotate(100, false);
//					ParcoursMain.leftMotor.endSynchronization();
				
				} while (res[0] < 0.2);
				
				//ParcoursMain.leftMotor.rotate(-120, false);
//				sensorMover.rotateTo(0, false);
			}
			
			//line is right
			else if (diff > eps) {
				LCD.clear(6);
				LCD.drawString("Rechts:  " + Float.toString(diff).substring(0, 3), 4, 6);
				ParcoursMain.leftMotor.stop(true);
				ParcoursMain.rightMotor.stop();
				sensorMover.rotateTo(0, false);
				

				do {
					redMode.fetchSample(res, 0);
					
//					ParcoursMain.leftMotor.startSynchronization();
					ParcoursMain.leftMotor.rotate(30, true);
					ParcoursMain.rightMotor.rotate(-100, false);
//					ParcoursMain.leftMotor.endSynchronization();
					
				} while (res[0] < 0.2);
//				sensorMover.rotateTo(0, false);
			}
			
			else {
				LCD.clear(6);
				LCD.drawString("Else:  " + Float.toString(diff).substring(0, 3), 4, 6);
				
				
				
				ParcoursMain.leftMotor.stop(true);
				ParcoursMain.rightMotor.stop();
				
				ParcoursMain.leftMotor.resetTachoCount();
				ParcoursMain.rightMotor.resetTachoCount();
				
				ParcoursMain.leftMotor.rotate(180, true);
				ParcoursMain.rightMotor.rotate(180);
				
				int rightTacho =  ParcoursMain.rightMotor.getTachoCount();
				int leftTacho =  ParcoursMain.leftMotor.getTachoCount();
				
				int TachoDiff = rightTacho - leftTacho;
				
				
				if (TachoDiff > 0) {
					ParcoursMain.leftMotor.rotate(TachoDiff);
				} else {
					ParcoursMain.rightMotor.rotate(-TachoDiff);
				}
				
			}

//			ParcoursMain.leftMotor.endSynchronization();
			//ParcoursMain.moveTo(null);
		}
		public void syncForward() {
			ParcoursMain.leftMotor.resetTachoCount();
			ParcoursMain.rightMotor.resetTachoCount();
			
			ParcoursMain.leftMotor.forward();
			ParcoursMain.rightMotor.forward();
			
		}
		
		public void syncStop() {
			ParcoursMain.rightMotor.stop(true);
			ParcoursMain.leftMotor.stop();
			
			int rightTacho =  ParcoursMain.rightMotor.getTachoCount();
			int leftTacho =  ParcoursMain.leftMotor.getTachoCount();
			
			int TachoDiff = rightTacho - leftTacho;
			
			
			if (TachoDiff > 0) {
				ParcoursMain.leftMotor.rotate(TachoDiff);
			} else {
				ParcoursMain.rightMotor.rotate(-TachoDiff);
			}
		}
		
		public float getRedValue() {
			float res[] = new float[1];
			
			redMode.fetchSample(res, 0);
			
			return res[0];
		}	
		
	},*/
	KOOPLINEFOLLOW("KoopLinienfolger") {
		private final double p = 1000;
		private final double offset = 0.3;
		
		private Port colorPort;
		private EV3ColorSensor color;
		private RegulatedMotor sensorMover;
		private SensorMode redMode;

		private int direction = 1;
		
		private final int baseRegulateSpeed = 250;
		private final int baseSpeed = 360;
		private final int sensorStopL = 75;
		private final int sensorStopR = 90;
		private final float blackEps = 0.1f;
		private final float whiteEps = 0.4f;
		private final double sumWhiteEps = 0.3;
		private final double diffEps = 0.08;
		
		private int ArraySize = 100;
		
		private ArrayList<Float> LeftSamples;
		private ArrayList<Float> RightSamples;
		
		public void init() {
			colorPort = ParcoursMain.brick.getPort("S1");
			color = new EV3ColorSensor(colorPort);
			sensorMover = new EV3MediumRegulatedMotor(MotorPort.D);
			redMode = color.getRedMode();
			ParcoursMain.leftMotor.setSpeed(baseSpeed);
			ParcoursMain.rightMotor.setSpeed(baseSpeed);
			sensorMover.setSpeed(600);
			LeftSamples = new ArrayList<Float>(ArraySize * 2);
			RightSamples = new ArrayList<Float>(ArraySize);
		}

		public void doStep() {
			LCD.drawString("V:" + ParcoursMain.getDistance(),2,2);
			
			if (ParcoursMain.getDistance() < 0.05) {
				syncStop();
				ParcoursMain.leftMotor.setSpeed(baseSpeed);
				ParcoursMain.rightMotor.setSpeed(baseSpeed);
				//Back off
				ParcoursMain.leftMotor.rotate(-600, true);
				ParcoursMain.rightMotor.rotate(-600, false);
				//Turn left
				ParcoursMain.rightMotor.rotate(500, true);
				ParcoursMain.leftMotor.rotate(-500, false);
				//Move
				ParcoursMain.leftMotor.rotate(700, true);
				ParcoursMain.rightMotor.rotate(700, false);
				//Turn right
				ParcoursMain.rightMotor.rotate(-450, true);
				ParcoursMain.leftMotor.rotate(450, false);
				//Forward
				ParcoursMain.leftMotor.rotate(1700, true);
				ParcoursMain.rightMotor.rotate(1700, false);
				//Turn right
				ParcoursMain.rightMotor.rotate(-450, true);
				ParcoursMain.leftMotor.rotate(450, false);
				//Move
				ParcoursMain.leftMotor.rotate(600, true);
				ParcoursMain.rightMotor.rotate(600, false);
				//Turn left
				ParcoursMain.rightMotor.rotate(450, true);
				ParcoursMain.leftMotor.rotate(-450, false);
				color.close();
				ParcoursMain.moveTo(MAILMAN);
			} else if (getRedValue() > blackEps) {
				//LCD.drawString("regulated",4,6);
				regulatedLineTask();
				//LCD.clear(6);
			} else {
				syncStop();
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
					
					sensorMover.rotateTo(0, true);
					
					//Lücke
					if (sensorDirection == 0){
						ParcoursMain.leftMotor.setSpeed(baseSpeed);
					
						ParcoursMain.rightMotor.setSpeed(baseSpeed);
						
						ParcoursMain.leftMotor.rotate(100, true);
						ParcoursMain.rightMotor.rotate(100, false);
					}
					//Korrektur
					else {
						while(getRedValue() < whiteEps) {
							LCD.drawString("K:" + getRedValue(),2,4);
							//Links
							if(sensorDirection == 1) {
								ParcoursMain.rightMotor.rotate(100, false);
							} 
							//rechts
							else if(sensorDirection == -1) {
								ParcoursMain.leftMotor.rotate(30, true);
								ParcoursMain.rightMotor.rotate(-100, false);
							}
						}
					}	
					
					break;
				}
				
			} 
			
			sensorMover.rotateTo(0, true);
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
			int y = (int) ((getRedValue() - offset) * p);
			
			ParcoursMain.leftMotor.setSpeed(baseRegulateSpeed + y);
			ParcoursMain.rightMotor.setSpeed(baseRegulateSpeed - y);
			ParcoursMain.leftMotor.forward();
			ParcoursMain.rightMotor.forward();
		}
		
		public void rotateSensorTask() {
			if(direction == 1) {
				sensorMover.rotateTo(sensorStopL * direction, true);
			} else {
				sensorMover.rotateTo(sensorStopR * direction, true);
			}
		}
		
		public int readSensorTask() {
			
			float currentValue = getRedValue();
			if(sensorMover.getTachoCount() > 0) {
				LeftSamples.add(currentValue);
			} else {
				RightSamples.add(currentValue);
			}
			
			
//			if(currentValue > blackEps) {
//				return direction;
//			}
			return 0;
		}
		
		
		public boolean checkTachoTask() {
			if(sensorMover.getTachoCount() >= sensorStopL) {
				direction = -1;
				return false;
			} else if(sensorMover.getTachoCount() <= -(sensorStopR)) {
				return true;
			}
			return false;
		}
		
		public float getRedValue() {
			float res[] = new float[1];
			
			redMode.fetchSample(res, 0);
			
			return res[0];
		}	
		
		
		public void syncForward() {
			ParcoursMain.leftMotor.resetTachoCount();
			ParcoursMain.rightMotor.resetTachoCount();
			
			ParcoursMain.leftMotor.forward();
			ParcoursMain.rightMotor.forward();
			
		}
		
		public void syncStop() {
			ParcoursMain.rightMotor.stop(true);
			ParcoursMain.leftMotor.stop();
		}
		
		public float calibrateWhite() {
			double leftAvg = calculateAverage(LeftSamples);
			double rightAvg = calculateAverage(RightSamples);
			
			double diffAvg = leftAvg - rightAvg;
			
			
			LeftSamples = new ArrayList<Float>(ArraySize * 2);
			RightSamples = new ArrayList<Float>(ArraySize);
			return 0.4f;
		}
	}
	,
	AVOIDCOLLISION("Avoid") {
		public void init() {
		
		}
		public void doStep() {
		}
	},
	/*COUNT("Count") {
		private int cnt;
		public void init() {
			cnt=0;
		}
		public void doStep() {
			cnt++;
			Delay.msDelay(1000);
			ParcoursMain.lcd.drawString(Integer.toString(cnt), 4, 3);
			if(cnt>10)
				ParcoursMain.moveTo(LOOP);
		}
	},
	
	LOOP("Loop forever") {
		private int cnt;
		public void init() { cnt = 0; }
		public void doStep() {
			cnt++;
			Delay.msDelay(1000);
			ParcoursMain.lcd.drawString(Integer.toString(cnt), 4, 3);
		}
	},
	 FORWARD("Drive forward") {
		private int cnt;
		public void init() { cnt = 0; }
		public void doStep() {

			ParcoursMain.leftMotor.synchronizeWith(new RegulatedMotor[]{ParcoursMain.rightMotor});
			
			ParcoursMain.leftMotor.setSpeed(360);
			ParcoursMain.rightMotor.setSpeed(360);
			
			ParcoursMain.leftMotor.startSynchronization();
			ParcoursMain.rightMotor.forward();
			ParcoursMain.leftMotor.forward();
			ParcoursMain.leftMotor.endSynchronization();
			
			Delay.msDelay(2000);
			
//			ParcoursMain.leftMotor.setSpeed(0);
//			ParcoursMain.rightMotor.setSpeed(0);
			
//			ParcoursMain.leftMotor.startSynchronization();
			ParcoursMain.rightMotor.stop(true);
			ParcoursMain.leftMotor.stop();
//			ParcoursMain.leftMotor.endSynchronization();			
			
			LCD.drawString("Rechts: " + ParcoursMain.leftMotor.getTachoCount(), 6, 4);
			LCD.drawString("Links: " + ParcoursMain.rightMotor.getTachoCount(), 6, 5);
			
			
			Delay.msDelay(5000);
			
		}
	},
	LINEFOLLOW_REGULATED("Follow line using regulator"){
		private Port colorPort;
		private EV3ColorSensor color;
		private RegulatedMotor sensorMover;
		private SensorMode redMode;
		private final double p = 1000;
		private final double offset = 0.3;
		public void init() {
			colorPort = ParcoursMain.brick.getPort("S1");
			color = new EV3ColorSensor(colorPort);
			redMode = color.getRedMode();
			ParcoursMain.leftMotor.setSpeed(250);
			ParcoursMain.rightMotor.setSpeed(250);
			ParcoursMain.leftMotor.forward();
			ParcoursMain.rightMotor.forward();
		}
		public void doStep() {
			float[] sample = new float[1];
			redMode.fetchSample(sample, 0);
			int y = (int) ((sample[0] - offset) * p);
			LCD.drawInt(y, 4, 6);
			
			ParcoursMain.leftMotor.setSpeed(180 + y);
			ParcoursMain.rightMotor.setSpeed(180 - y);
			ParcoursMain.leftMotor.forward();
			ParcoursMain.rightMotor.forward();
		}
	},*/
	MAILMAN("Hermes") {
		private Port colorPort;
		private Port touchPort;
		private EV3ColorSensor color;
		private EV3TouchSensor touch;
		private RegulatedMotor sensorMover;
		private SensorMode redMode;
		private SensorMode touchMode;
		
		private int leftTacho;
		private int rightTacho;
		
		public void init() {
			colorPort = ParcoursMain.brick.getPort("S1");
			color = new EV3ColorSensor(colorPort);
			redMode = color.getRedMode();
			touchPort = ParcoursMain.brick.getPort("S2");
			touch = new EV3TouchSensor(touchPort);
			touchMode = touch.getTouchMode();
				
			ParcoursMain.rightMotor.setSpeed(1000);
			ParcoursMain.leftMotor.setSpeed(1000);
			ParcoursMain.leftMotor.rotate(4500, true);
			ParcoursMain.rightMotor.rotate(4500, false);

			ParcoursMain.leftMotor.rotate(-20, true);
			ParcoursMain.rightMotor.rotate(20, false);
			
			ParcoursMain.leftMotor.resetTachoCount();
			ParcoursMain.rightMotor.resetTachoCount();
			
			leftTacho = ParcoursMain.leftMotor.getTachoCount();
			rightTacho = ParcoursMain.rightMotor.getTachoCount();
			
		}
		public void doStep() {
			ParcoursMain.rightMotor.backward();
			ParcoursMain.leftMotor.forward();
			float dis = ParcoursMain.getDistance();
			
			LCD.drawString("Distantce: " + dis, 2, 4);
			
			if(dis < 0.6) {
				ParcoursMain.rightMotor.stop(true);
				ParcoursMain.leftMotor.stop();
				
				ParcoursMain.HERMES_LEFT_DELTA = (leftTacho - ParcoursMain.leftMotor.getTachoCount());
				ParcoursMain.HERMES_RIGHT_DELTA = (rightTacho - ParcoursMain.rightMotor.getTachoCount());
				
				//Stop after finding box
				Delay.msDelay(250);
				
				//Back off
				ParcoursMain.leftMotor.rotate(-600, true);
				ParcoursMain.rightMotor.rotate(-600, false);
				
				ParcoursMain.rightMotor.rotate(-1100, true);
				ParcoursMain.leftMotor.rotate(1100, false);
				
				ParcoursMain.rightMotor.setSpeed(10000);
				ParcoursMain.leftMotor.setSpeed(10000);
				ParcoursMain.rightMotor.backward();
				ParcoursMain.leftMotor.backward();
				
//				Delay.msDelay(3000);
				
				int abort = 0;
				
				while(true) {
					float touched = getTouchValue();
					LCD.drawString("Touch " + touched, 2,5);
					if(touched == 1.0f) {
						if(abort > 1) {
							break;
						}
						abort = 1;
					} else if (touched == 0) {
						if(abort == 1)
							abort = 2;
					} else {
						break;
					}
				}

				ParcoursMain.rightMotor.stop(true);
				ParcoursMain.leftMotor.stop();
				color.close();
				ParcoursMain.moveTo(BRIDGE);
			}
		}
		
		public float getTouchValue() {
			float res[] = new float[1];
			
			touchMode.fetchSample(res, 0);
			
			return res[0];
		}	
	},
	BRIDGE("Bridge") {
		private Port colorPort;
		private EV3ColorSensor color;
		private RegulatedMotor sensorMover;
		private SensorMode rgbMode;
		
		private final int sensorStopL = 75;
		private final int sensorStopR = 90;
		
		private final double diffEps = 0.08;
		private final double sumBlueEps = 0.3;
		
		private final int ArraySize = 100;
		
		private ArrayList<Float> LeftSamples;
		private ArrayList<Float> RightSamples;
		
		private int leftDelta;
		private int rightDelta;
		
		private boolean blueFound = false;
		
		private final float coloreps = 0.05f;
		
		public void init() {
			colorPort = ParcoursMain.brick.getPort("S1");
			color = new EV3ColorSensor(colorPort);
			rgbMode = color.getRGBMode();
			
			LeftSamples = new ArrayList<Float>(ArraySize * 2);
			RightSamples = new ArrayList<Float>(ArraySize);
			
			leftDelta = ParcoursMain.HERMES_LEFT_DELTA;
			rightDelta = ParcoursMain.HERMES_RIGHT_DELTA;
			
			ParcoursMain.leftMotor.resetTachoCount();
			ParcoursMain.rightMotor.resetTachoCount();
			
			while((ParcoursMain.leftMotor.getTachoCount() - leftDelta) > 5) {
				ParcoursMain.rightMotor.forward();
				ParcoursMain.leftMotor.backward();
			} 

			ParcoursMain.rightMotor.stop(true);
			ParcoursMain.leftMotor.stop();
			
			
			LCD.clear();
		}
		public void doStep() {
			float[] rgb = getRGBValue();
			
			LCD.drawString("R_Delta: " + rightDelta, 2, 1);
			LCD.drawString("L_Delta: " + leftDelta, 2, 2);
			LCD.drawString("R_Tacho: " + ParcoursMain.rightMotor.getTachoCount(), 2, 3);
			LCD.drawString("L_Tacho: " + ParcoursMain.leftMotor.getTachoCount(), 2, 4);
			
			
//			LCD.drawString("red: " + rgb[0], 2, 3);
//			LCD.drawString("green: " + rgb[1], 2, 4);
//			LCD.drawString("blue: " + rgb[2], 2, 5);
//			
//			LCD.drawString("color:   " + getColor(), 2,6 );
			
			while(!blueFound) {
				
				LCD.drawString("Blue? :   " + isBlueLine() + "!", 2,6 );
				ParcoursMain.rightMotor.forward();
				ParcoursMain.leftMotor.forward();
				
				if(isBlueLine()) {
					blueFound = true;
					ParcoursMain.rightMotor.stop(true);
					ParcoursMain.leftMotor.stop();
					readBlue();
					LCD.drawInt(getDirection(), 2, 7);
					break;
				}	
			}
			
			
			Delay.msDelay(50);
		}
		
		
		public float[] getRGBValue() {
			float res[] = new float[3];
			
			rgbMode.fetchSample(res, 0);
			
			return res;
		}
		
		public void readBlue() {
			sensorMover.rotateTo(-sensorStopL, true);
			while(sensorMover.getTachoCount() < sensorStopL) {
				readSensorTask();
			}
			sensorMover.rotateTo(sensorStopR, true);
			while(sensorMover.getTachoCount() > -(sensorStopR)) {
				readSensorTask();
			}
			
		}
			
						
		
		/*
		 * return -1 as error value.
		 * return 0 for red,
		 * return 1 for green,
		 * return 2 for blue.
		 */
		public int getColor() {
			float[] rgb = getRGBValue();
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
			
			float currentValue = getColor();
			if(sensorMover.getTachoCount() > 0) {
				LeftSamples.add(currentValue);
			} else {
				RightSamples.add(currentValue);
			}
		}	
		
		public boolean isBlueLine() {
			float[] rgb = getRGBValue();
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
			} else if (diffAvg < -diffEps || ((leftAvg+rightAvg) > sumBlueEps)) {
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
	};

	public String name;
	ParcoursSegment(String name) {
		this.name = name;
	}
	public abstract void init();
	public abstract void doStep();
}
