package edu.kit.h2t.mindstorms.group2;

import lejos.utility.Delay;

import java.util.ArrayList;

import lejos.ev3.tools.LCDDisplay;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

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
		private double whiteEps = 0.4f;
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
			calibrateWhite();
		}

		public void doStep() {
			LCD.drawString("V:" + ParcoursMain.getDistance(),2,2);
			LCD.drawString("W:" + whiteEps,2,3);
			
			if (ParcoursMain.getDistance() < 0.05) {
				syncStop();
				ParcoursMain.leftMotor.setSpeed(baseSpeed);
				ParcoursMain.rightMotor.setSpeed(baseSpeed);
				//Back off
				ParcoursMain.leftMotor.rotate(-400, true);
				ParcoursMain.rightMotor.rotate(-400, false);
				//Turn left
				ParcoursMain.rightMotor.rotate(500, true);
				ParcoursMain.leftMotor.rotate(-500, false);
				//Move
				ParcoursMain.leftMotor.rotate(800, true);
				ParcoursMain.rightMotor.rotate(800, false);
				//Turn right
				ParcoursMain.rightMotor.rotate(-440, true);
				ParcoursMain.leftMotor.rotate(440, false);
				//Forward
				ParcoursMain.leftMotor.rotate(1400, true);
				ParcoursMain.rightMotor.rotate(1400, false);
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
				sensorMover.close();
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
								ParcoursMain.rightMotor.rotate(100, true);
							} 
							//rechts
							else if(sensorDirection == -1) {
								ParcoursMain.leftMotor.rotate(150, true);
								ParcoursMain.rightMotor.rotate(-100, true);
							}
						}
						syncStop();
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
			ArrayList<Float>MidSamples = new ArrayList<Float>(ArraySize);
			
			int offset_angle = 15;
			sensorMover.rotateTo(offset_angle);
			sensorMover.rotateTo(sensorStopL, true);
			while(sensorMover.getTachoCount() < sensorStopL) {
				readSensorTask();
			}
			sensorMover.rotateTo(offset_angle);
			sensorMover.rotateTo(-offset_angle, true);
			while(sensorMover.getTachoCount() > -(offset_angle)) {
				float currentValue = getRedValue();
				MidSamples.add(currentValue);
			}
			
			sensorMover.rotateTo(-(sensorStopR), true);
			while(sensorMover.getTachoCount() > -(sensorStopR)) {
				readSensorTask();
			}
			
			sensorMover.rotateTo(0);
			
			
			
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
	,
	SOUND("Beep") {
		public void init() {
			Sound.playTone(Sound.BEEP, 1000, 100);
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
			ParcoursMain.leftMotor.rotate(4800, true);
			ParcoursMain.rightMotor.rotate(4800, false);

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
			
			LCD.drawString("Distance: " + dis, 2, 4);
			
			if(dis < 0.4) {
				ParcoursMain.rightMotor.stop(true);
				ParcoursMain.leftMotor.stop();
				
				ParcoursMain.HERMES_LEFT_DELTA = (leftTacho - ParcoursMain.leftMotor.getTachoCount());
				ParcoursMain.HERMES_RIGHT_DELTA = (rightTacho - ParcoursMain.rightMotor.getTachoCount());
				
				//Stop after finding box
				Delay.msDelay(250);
				
				//Back off
				//ParcoursMain.leftMotor.rotate(-300, true);
				//ParcoursMain.rightMotor.rotate(-300, false);
				
				ParcoursMain.rightMotor.rotate(-1200, true);
				ParcoursMain.leftMotor.rotate(1200, false);
				
				ParcoursMain.rightMotor.setSpeed(10000);
				ParcoursMain.leftMotor.setSpeed(10000);
				ParcoursMain.rightMotor.backward();
				ParcoursMain.leftMotor.backward();
				
//				Delay.msDelay(3000);
				
				int abort = 0;
				
				while(true) {
					float touched = getTouchValue();
					LCD.drawString("Touch " + abort, 2,5);
					if(touched == 1.0f) {
						if(abort >= 0)
							abort++;
						if(abort > 600) {
							break;
						}
					} else if (touched == 0) {
					} else {
						break;
					}
				}

				ParcoursMain.rightMotor.stop(true);
				ParcoursMain.leftMotor.stop();
				
				//Back off
//				ParcoursMain.leftMotor.rotate(600, true);
//				ParcoursMain.rightMotor.rotate(600, false);
				
				color.close();
				ParcoursMain.moveTo(FIND_BRIDGE);
			}
		}
		
		public float getTouchValue() {
			float res[] = new float[1];
			
			touchMode.fetchSample(res, 0);
			
			return res[0];
		}	
	},
	FIND_BRIDGE("Find Bridge") {
		private Port colorPort;
		private EV3ColorSensor color;
		private RegulatedMotor sensorMover;
		private SensorMode rgbMode;
		
		private final int sensorStopL = 75;
		private final int sensorStopR = 90;
		
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
			colorPort = ParcoursMain.brick.getPort("S1");
			color = new EV3ColorSensor(colorPort);
			rgbMode = color.getRGBMode();
			
			LeftSamples = new ArrayList<Float>(ArraySize * 2);
			RightSamples = new ArrayList<Float>(ArraySize);
			
			leftDelta = ParcoursMain.HERMES_LEFT_DELTA;
			rightDelta = ParcoursMain.HERMES_RIGHT_DELTA;
			
			sensorMover = new EV3MediumRegulatedMotor(MotorPort.D);
			
			ParcoursMain.leftMotor.resetTachoCount();
			ParcoursMain.rightMotor.resetTachoCount();
			
			ParcoursMain.rightMotor.setSpeed(200);
			ParcoursMain.leftMotor.setSpeed(200);

			ParcoursMain.leftMotor.rotate(300, true);
			ParcoursMain.rightMotor.rotate(300, false);
			
			LCD.clear();
			
//			correctCourseTacho();
//			correctNaive();
//			correctCourseWhite();
			correctDistance();
			
			ParcoursMain.rightMotor.setSpeed(500);
			ParcoursMain.leftMotor.setSpeed(500);
		
		}
		public void doStep() {
			float[] rgb = getRGBValue();
			float red = rgb[0];
			float green = rgb[1];
			float blue = rgb[2];
					
//			LCD.drawString("R_Delta: " + rightDelta, 2, 1);
//			LCD.drawString("L_Delta: " + leftDelta, 2, 2);
//			LCD.drawString("R_Tacho: " + ParcoursMain.rightMotor.getTachoCount(), 2, 3);
//			LCD.drawString("L_Tacho: " + ParcoursMain.leftMotor.getTachoCount(), 2, 4);
			
			
			LCD.drawString("red: " + red, 2, 3);
			LCD.drawString("green: " + green, 2, 4);
			LCD.drawString("blue: " + blue, 2, 5);
//			
//			LCD.drawString("color:   " + getColor(), 2,6 );
			
			
				
			LCD.drawString("Blue? :   " + isBlueLine() + "!", 2,6 );
			ParcoursMain.rightMotor.forward();
			ParcoursMain.leftMotor.forward();
			
			if(isBlueLine()) {
				ParcoursMain.rightMotor.stop(true);
				ParcoursMain.leftMotor.stop();
				readBlue();
				double diff = getDirectionDiff();
				LCD.drawString("D: " + diff, 2, 7);
				allignBlue(diff);
				color.close();
				sensorMover.close();
				ParcoursMain.moveTo(BRIDGE);
			}	
		
			
			Delay.msDelay(50);
			
		}
		
		
		private void correctNaive() {
			ParcoursMain.rightMotor.rotate(250, true);
			ParcoursMain.leftMotor.rotate(-250, false);
		}
		
		private void correctCourseTacho() {
			while((ParcoursMain.leftMotor.getTachoCount() - leftDelta) > 5) {
				ParcoursMain.rightMotor.forward();
				ParcoursMain.leftMotor.backward();
			} 

			ParcoursMain.rightMotor.stop(true);
			ParcoursMain.leftMotor.stop();
			
		}
		
		private void correctDistance() {
			LCD.clear();
			while(ParcoursMain.getDistance() < 0.9) {
				//Turn left
				ParcoursMain.rightMotor.forward();
				ParcoursMain.leftMotor.backward();
				
			}
			LCD.drawString("D1: " + ParcoursMain.getDistance(), 2, 6);
			Sound.beep();
			
			while(ParcoursMain.getDistance() > 0.8) {
				//Turn left
				ParcoursMain.rightMotor.forward();
				ParcoursMain.leftMotor.backward();
				LCD.drawString("D2: " + ParcoursMain.getDistance(), 2, 7);
			}
			
			Sound.beep();
			
//			ParcoursMain.leftMotor.rotate(100, true);
//			ParcoursMain.rightMotor.rotate(-100, false);
			
			ParcoursMain.rightMotor.stop(true);
			ParcoursMain.leftMotor.stop();
		}
		
		private void correctCourseWhite() {
			while(true) {
				
				LCD.drawString("White? :   " + isBlueLine() + "!", 2,6 );
				ParcoursMain.rightMotor.forward();
				ParcoursMain.leftMotor.forward();
				
				if(isBlueLine()) {
					ParcoursMain.rightMotor.stop(true);
					ParcoursMain.leftMotor.stop();
					readBlue();
					double diff = getDirectionDiff();
					LCD.drawString("D: " + diff, 2, 7);
					allignBlue(diff);
					break;
				}	
			}
		}
		
		public float[] getRGBValue() {
			float res[] = new float[3];
			
			rgbMode.fetchSample(res, 0);
			
			return res;
		}
		
		public void readBlue() {
			if(sensorMover != null) {
				sensorMover.rotateTo(sensorStopL, true);
				while(sensorMover.getTachoCount() < sensorStopL) {
					readSensorTask();
				}
				sensorMover.rotateTo(-sensorStopR, true);
				while(sensorMover.getTachoCount() > -(sensorStopR)) {
					readSensorTask();
				}
				sensorMover.rotateTo(0);
			} else {
				System.out.println("NULL!");
			}
		}
			
		
		/*  Diff = leftAvg - rightAvg
		 * 	Left means positive diff
		 *	Right means negative diff
		*/
		public void allignBlue(double diff) {
			int baseRotate = 360;
			ParcoursMain.leftMotor.rotate((int) (baseRotate * -diff), true);
			ParcoursMain.rightMotor.rotate((int) (baseRotate * diff), false);
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
	},
	
	BRIDGE("Bridge") {

		private Port colorPort;
		private EV3ColorSensor color;
		private RegulatedMotor sensorMover;
		private SensorMode redMode;
		private SampleProvider angleMode;
		private Port gyroPort;
		private EV3GyroSensor gyro;
		
		private int state = 0;
		
		private final int sensorStopL = 75;
		private final int sensorStopR = 90;
		
		private final double diffEps = 0.1;
		private final double sumBlueEps = 0.3;
		
		private final int ArraySize = 100;
		
		private ArrayList<Float> LeftSamples;
		private ArrayList<Float> RightSamples;
		
		public void init() {
			LCD.clear();
			colorPort = ParcoursMain.brick.getPort("S1");
			color = new EV3ColorSensor(colorPort);
			redMode = color.getRedMode();
			gyroPort = ParcoursMain.brick.getPort("S4");
			gyro = new EV3GyroSensor(gyroPort);
			angleMode = gyro.getAngleMode();
			
			LeftSamples = new ArrayList<Float>(ArraySize * 2);
			RightSamples = new ArrayList<Float>(ArraySize);
			
			sensorMover = new EV3MediumRegulatedMotor(MotorPort.D);
//			sensorMover.rotateTo(sensorStopL);
			gyro.reset();
			Sound.buzz();
			
		}

		public void doStep() {
			LCD.drawString("Value: " + getRedValue(), 2, 3);
			LCD.drawString("Void: " + isVoid(), 2, 4);
			LCD.drawString("Angle: " + getAngle(), 2, 5);
			LCD.drawString("State: " + state, 2, 6);
			
			switch(state) {
			
			//Get to Bridge
			case 0:
				getToBridge();
				state++;
				break;
			
			//Drive up	
			case 1:
				syncForward();
				if(getAngle() < 5) {
					state++;
					Sound.beep();
				}
				break;
			
			//Correction	
			case 2:	
				if(isVoid()) {
					syncStop();
					
					//Backoff
					ParcoursMain.rightMotor.rotate(-100, true);
					ParcoursMain.leftMotor.rotate(-100, false);
					
					//Turn left
					ParcoursMain.rightMotor.rotate(600, true);
					ParcoursMain.leftMotor.rotate(-600, false);
				} else {
					syncForward();
				}
				if(getAngle() < -5) {
					state++;
					Sound.beep();
				}
				break;
			//Drive down	
			case 3:
				state++;
				break;
			default:
				syncStop();
				break;
			
			}
//			
//			//Drive up 
//			if (getAngle() > 5) {
//				syncForward();
//			} 
//			//Drive down
//			else if (getAngle() < -5 ) {
//				sensorMover.rotateTo(-sensorStopR);
//				syncForward();
//			} 
//			else if(isVoid()) {
//				//Backoff
//				ParcoursMain.rightMotor.rotate(-100, true);
//				ParcoursMain.leftMotor.rotate(-100, false);
//				
//				//Turn left
//				ParcoursMain.rightMotor.rotate(600, true);
//				ParcoursMain.leftMotor.rotate(-600, false);
//			}
//			else {
//				syncStop();
//			}
			
		}
		
		public void syncForward() {
			ParcoursMain.leftMotor.forward();
			ParcoursMain.rightMotor.forward();
		}
		
		public void syncStop() {
			ParcoursMain.leftMotor.stop(true);
			ParcoursMain.rightMotor.stop();
			
		}
		
		public void getToBridge() {
			while(getAngle() < 5) {
				syncForward();
			}
		}
		
		public float getAngle() {
			float res[] = new float[1];
			angleMode.fetchSample(res, 0);
			
			return res[0];
		}
		
		public boolean isVoid() {
			return getRedValue() < 0.02;
		}
		
		public float getRedValue() {
			float res[] = new float[1];
			
			redMode.fetchSample(res, 0);
			
			return res[0];
		}	
		
		
	}
	
	;

	
	
	public String name;
	ParcoursSegment(String name) {
		this.name = name;
	}
	public abstract void init();
	public abstract void doStep();
}
