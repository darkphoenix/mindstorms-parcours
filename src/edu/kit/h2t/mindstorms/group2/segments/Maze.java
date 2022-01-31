package edu.kit.h2t.mindstorms.group2.segments;

import edu.kit.h2t.mindstorms.group2.ParcoursMain;
import edu.kit.h2t.mindstorms.group2.RobotUtil;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Maze implements ParcoursSegment {

	private boolean foundWhite;
	private boolean foundRed;
	private boolean turnDirectionLeft;

	private boolean drivingForward = true;

	private int speedOffset = 25;

	private int leftTachoCache;
	private int rightTachoCache;

	private int baseSpeed;
	private float SpeedModifier = 1.3f;
	
	private float whiteEps = 0.2f;
	private float redEps = 0.07f;

	public void init() {
		baseSpeed = (int) (RobotUtil.baseSpeed * SpeedModifier);
		
		foundRed = false;
		foundWhite = false;

		turnDirectionLeft = true;

		drivingForward = true;

		RobotUtil.setMotorSpeed(baseSpeed);

		while (RobotUtil.getDistance() > 0.15 && RobotUtil.chk()) {
			RobotUtil.syncForward();
		}
		RobotUtil.turn(1150, false);
		RobotUtil.spin(600);
	}

	public void doStep() {
		LCD.drawString("White: " + foundWhite, 2, 2);
		LCD.drawString("Red: " + foundRed, 2, 3);

		if (foundRed && foundWhite) {
			Sound.twoBeeps();
//			RobotUtil.oneUpSound();
			RobotUtil.syncStop();
			RobotUtil.abort();
		} else {
			// driveRoutineTurns();
			driveRoutineZigZag();
			searchRoutine();
		}

	}

	private void searchRoutine() {
		float[] color = RobotUtil.getRGB();

		float red = color[0];
		float green = color[1];
		float blue = color[2];

		RobotUtil.lcd.drawString("red: " + red, 2, 4);
		RobotUtil.lcd.drawString("green: " + green, 2, 5);
		RobotUtil.lcd.drawString("blue: " + blue, 2, 6);

		if (isRed(color) && !isWhite(color)) {
			// Beep on first sound
			if (!foundRed) {
				Sound.beep();
			}
			foundRed = true;
		}

		if (isWhite(color)) {
			// Beep on first found
			if (!foundWhite) {
				Sound.beep();
			}

			foundWhite = true;
		}
	}

	private boolean isWhite(float[] rgb) {
		float red = rgb[0];
		float green = rgb[1];
		float blue = rgb[2];

		return blue > whiteEps && green > whiteEps;
	}

	private boolean isRed(float[] rgb) {
		float red = rgb[0];
		float green = rgb[1];
		float blue = rgb[2];

		return red > redEps && green < redEps;
	}

	private void driveRoutineZigZag() {

		if (drivingForward) {
			if (RobotUtil.getDistance() < 0.15) {
				drivingForward = false;
				RobotUtil.syncBackward();
				return;
			} else {
				RobotUtil.setMotorSpeed(baseSpeed, baseSpeed);
				RobotUtil.syncForward();
				return;
			}

		} else if (!drivingForward) {
			if (RobotUtil.getTouch()) {
				drivingForward = true;
				Delay.msDelay((long) (1000 / SpeedModifier));
				RobotUtil.syncForward();
				return;
			} else {
				RobotUtil.setMotorSpeed(baseSpeed + speedOffset, baseSpeed);
				RobotUtil.syncBackward();
				return;
			}
		}

	}

	private void driveRoutineTurns() {
		if (RobotUtil.getDistance() < 0.15) {
			// Backoff
			RobotUtil.rightMotor.rotate(-100, true);
			RobotUtil.leftMotor.rotate(-100, false);

			if (turnDirectionLeft) {
				// Turn left
//				RobotUtil.turn(2400, false);
				RobotUtil.spin(1200);
			} else {
				// Turn right
//				RobotUtil.turn(2400, true);
				RobotUtil.spin(-1200);
			}

			turnDirectionLeft = !turnDirectionLeft;

			while (!RobotUtil.getTouch() && RobotUtil.chk()) {
				searchRoutine();
				RobotUtil.syncBackward();
			}

		}

		RobotUtil.syncForward();
	}

}
