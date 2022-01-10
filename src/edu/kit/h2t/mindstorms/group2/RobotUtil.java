package edu.kit.h2t.mindstorms.group2;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
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

public class RobotUtil {
	private static boolean isRGB = true;
	private static Port colourPort;
	private static EV3ColorSensor colourSensor;
	private static SensorMode colourMode;
	
	private static Port ultraPort;
	private static EV3UltrasonicSensor ultraSensor;
	private static SensorMode ultrasonic;

	private static Port touchPort;
	private static EV3TouchSensor touch;
	private static SensorMode touchMode;

	private static SampleProvider angleMode;
	private static Port gyroPort;
	private static EV3GyroSensor gyro;

	public static EV3 brick;
	public static TextLCD lcd;
	public static RegulatedMotor leftMotor;
	public static RegulatedMotor rightMotor;
	public static RegulatedMotor sensorMover;
	
	private static final int sensorStopL = 75;
	private static final int sensorStopR = 90;
	
	public static void init() {
		//init brick
		brick = (EV3) BrickFinder.getLocal();
		lcd = brick.getTextLCD();
		
		lcd.drawString("Initializing...", 2, 2);
		
		//init colour
		colourPort = brick.getPort("S1");
		colourSensor = new EV3ColorSensor(colourPort);
		useRed();
		
		//init touch
		lcd.drawString("Touch", 2, 3);
		touchPort = brick.getPort("S2");
		touch = new EV3TouchSensor(touchPort);
		touchMode = touch.getTouchMode();
		
		//init gyro
		lcd.drawString("Gyro ", 2, 3);
		gyroPort = brick.getPort("S4");
		gyro = new EV3GyroSensor(gyroPort);
		angleMode = gyro.getAngleMode();
		gyro.reset();
		
		//init ultrasonic
		lcd.drawString("Ultrasonic", 2, 3);
		boolean initialised = false;
		while (!initialised)
			try {
				ultraPort = brick.getPort("S3");
				ultraSensor = new EV3UltrasonicSensor(ultraPort);
				ultrasonic = (SensorMode) ultraSensor.getDistanceMode();
				initialised = true;
			} catch (Exception e) {}
		

		lcd.drawString("Motors", 2, 3);
		leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		rightMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		sensorMover = new EV3MediumRegulatedMotor(MotorPort.D);
		sensorMover.resetTachoCount();
		sensorMover.setSpeed(600);
		
		lcd.clear();
	}
	
	private static void useRed() {
		if(isRGB)
			colourMode = colourSensor.getRedMode();
		isRGB = false;
	}
	
	private static void useRGB() {
		if(!isRGB)
			colourMode = colourSensor.getRGBMode();
		isRGB = true;
	}
	
	public static float getRed() {
		useRed();
		float red[] = new float[1];
		colourMode.fetchSample(red, 0);
		return red[0];
	}
	
	public static float[] getRGB() {
		useRGB();
		float rgb[] = new float[3];
		colourMode.fetchSample(rgb, 0);
		return rgb;
	}
	
	public static float getDistance() {
		float dist[] = new float[1];
		ultrasonic.fetchSample(dist, 0);
		return dist[0];
	}
	

	public static boolean getTouch() {
		float res[] = new float[1];
		
		touchMode.fetchSample(res, 0);
		
		return res[0] == 1.0f;
	}
	
	public static float getAngle() {
		float angle[] = new float[1];
		angleMode.fetchSample(angle, 0);
		return angle[0];
	}
	
	public static void syncForward() {
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		
		leftMotor.forward();
		rightMotor.forward();
		
	}
	
	public static void syncForwardCorrectionTask() {
		int rightTacho =  rightMotor.getTachoCount();
		int leftTacho =  leftMotor.getTachoCount();
		
		int TachoDiff = rightTacho - leftTacho;
		
		
		if (TachoDiff > 50) {
			syncStop();
			leftMotor.rotate(TachoDiff);
		} else if (TachoDiff < -50) {
			syncStop();
			rightMotor.rotate(-TachoDiff);
		}
		
		leftMotor.forward();
		rightMotor.forward();
	}
	
	public static void setMotorSpeed(int speed) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
	}
	
	public static void syncStop() {
		rightMotor.stop(true);
		leftMotor.stop();
	}
	
	/**
	 * Spin in place by certain amount of motor degrees
	 * @param degrees Motor degrees, positive is clockwise turn, 440 is roughly 90 degree turn
	 */
	public static void spin(int degrees) {
		rightMotor.rotate(-degrees, true);
		leftMotor.rotate(degrees, false);
	}
	
	public static void sensorMoverLeft() {
		sensorMover.rotateTo(sensorStopL, true);
	}
	
	public static void sensorMoverCenter() {
		sensorMover.rotateTo(0, true);
	}
	
	public static void sensorMoverRight() {
		sensorMover.rotateTo(sensorStopR, true);
	}
	
	public static boolean isSensorMoverLeft() {
		return sensorMover.getTachoCount() >= sensorStopL;
	}
	
	public static boolean isSensorMoverRight() {
		return sensorMover.getTachoCount() >= sensorStopR;
	}
	
	public static int sensorMoverTacho() {
		return sensorMover.getTachoCount();
	}
}
