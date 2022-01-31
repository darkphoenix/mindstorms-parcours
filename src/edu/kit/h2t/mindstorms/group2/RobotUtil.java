package edu.kit.h2t.mindstorms.group2;

import java.io.File;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
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
	
	public static enum Notes {
		D4(293.66),
		G4(392.00),
		AS4(425.30),
		B4(466.16),
		H4(493.88),
		C5(532.25),
		D5(587.33),
		ES5(622.25),
		F5(698.46),
		G5(783.99),
		AS5(850.61),
		B5(932.33),
		H5(987.77),
		C6(1046.50),
		D6(1174.66);

	    public final double freq;

	    private Notes(double freq) {
	        this.freq = freq;
	    }
	}
	
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
	private static final int sensorStopR = -90;
	
	public static final int baseSpeed = 360;
	
	private static boolean abort = false;
	
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
		boolean initialisedGyro = false;
		while (!initialisedGyro)
			try {
				gyroPort = brick.getPort("S4");
				gyro = new EV3GyroSensor(gyroPort);
				angleMode = gyro.getAngleMode();
				gyro.reset();
				initialisedGyro = true;
			} catch (Exception e) {}
		

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
	
	public static void resetAngle() {
		gyro.close();
		boolean initialisedGyro = false;
		while (!initialisedGyro)
			try {
				gyroPort = brick.getPort("S4");
				gyro = new EV3GyroSensor(gyroPort);
				angleMode = gyro.getAngleMode();
				gyro.reset();
				initialisedGyro = true;
			} catch (Exception e) {}
	}
	
	public static int getLeftTacho() {
		return leftMotor.getTachoCount();
	}
	
	public static int getRightTacho() {
		return rightMotor.getTachoCount();
	}
	
	public static int getAbsTachoDiff() {
		return Math.abs(getLeftTacho() - getRightTacho());
	}
	
	public static void syncForward() {
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		
		leftMotor.forward();
		rightMotor.forward();
		
	}
	
	public static void syncBackward() {
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		
		leftMotor.backward();
		rightMotor.backward();
		
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
	
	public static void setMotorSpeed(int leftMotorSpeed, int rightMotorSpeed) {
		leftMotor.setSpeed(leftMotorSpeed);
		rightMotor.setSpeed(rightMotorSpeed);	
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
	
	/**
	 * 
	 * Make a turn with only one motor moving.
	 * @param degrees Motor degrees.
	 * @param rightTurn if true, robot makes a right turn. Turns left otherwise.
	 */
	public static void turn(int degrees, boolean rightTurn) {
		if(rightTurn) {
			rightMotor.stop();
			leftMotor.rotate(degrees);
		} else {
			leftMotor.stop();
			rightMotor.rotate(degrees);
		}
	}

	/*	
	g4, h4, d5, g5, h5
	 
	as4, c5, es5, as5, c6

	b4, d4, f5, b5, d6
*/
	public static void oneUpSound() {
		Notes[] melody ={Notes.G4, Notes.H4, Notes.D4, Notes.G5, Notes.H5, 
				Notes.AS4, Notes.C5, Notes.ES5, Notes.AS5, Notes.C6,
				Notes.B4, Notes.D4, Notes.F5, Notes.B5, Notes.D6};
		
		for(Notes note : melody) {
			Sound.playNote(Sound.PIANO, (int) note.freq, 2);
		}
		
	}
	
	public static void gameoverSound() {
		try {
			File sound = new File("/sounds/smb_gameover");
			Sound.playSample(sound);
		} catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	public static void sensorMoverLeft() {
		sensorMover.rotateTo(sensorStopL, true);
	}
	
	public static void sensorMoverCenter() {
		sensorMoverCenter(true);
	}
	
	public static void sensorMoverCenter(boolean returnImmediate) {
		sensorMover.rotateTo(0, returnImmediate);
	}
	
	public static void sensorMoverRight() {
		sensorMover.rotateTo(sensorStopR, true);
	}
	
	public static boolean isSensorMoverLeft() {
		return sensorMover.getTachoCount() >= sensorStopL;
	}
	
	public static boolean isSensorMoverRight() {
		return sensorMover.getTachoCount() <= sensorStopR;
	}
	
	public static int sensorMoverTacho() {
		return sensorMover.getTachoCount();
	}
	
	public static boolean chk() {
		return !Button.ESCAPE.isDown() && !abort;
	}
	
	public static void abort(boolean p) {
		abort = p;
	}
	
	public static void abort() {
		abort(true);
	}
}
