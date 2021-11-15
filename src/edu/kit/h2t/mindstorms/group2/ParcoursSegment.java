package edu.kit.h2t.mindstorms.group2;

import lejos.utility.Delay;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;

public enum ParcoursSegment {
	LINEFOLLOW("Follow line") {
		private Port colorPort;
		private EV3ColorSensor color;
		private RegulatedMotor sensorMover;
		private SensorMode redMode;
		public void init() {
			colorPort = ParcoursMain.brick.getPort("S1");
			color = new EV3ColorSensor(colorPort);
			sensorMover = new EV3MediumRegulatedMotor(MotorPort.D);
			redMode = color.getRedMode();
			ParcoursMain.leftMotor.setSpeed(250);
			ParcoursMain.rightMotor.setSpeed(250);
			sensorMover.setSpeed(3600);
		}
		public void doStep() {
			//Drive on line
			float res[] = new float[1];
			ParcoursMain.leftMotor.synchronizeWith(new RegulatedMotor[]{ParcoursMain.rightMotor});
			do {
				redMode.fetchSample(res, 0);
				ParcoursMain.leftMotor.forward();
				ParcoursMain.rightMotor.forward();
			} while (res[0] > 0.4);
			ParcoursMain.leftMotor.stop();
			ParcoursMain.rightMotor.stop();
			
			//Lost line, let's find it
			float[] leftSample = new float[1];
			float[] rightSample = new float[1];
			
			sensorMover.rotateTo(-60, false);
			redMode.fetchSample(leftSample, 0);
			
			sensorMover.rotateTo(60, false);
			redMode.fetchSample(rightSample, 0);
			
			float diff = leftSample[0] - rightSample[0];
			
			sensorMover.rotateTo(0, false);
			
			//line is left
			if (diff < -0.1) {
				ParcoursMain.leftMotor.stop();
				ParcoursMain.rightMotor.stop();
				sensorMover.rotateTo(60, false);
				do {
					redMode.fetchSample(res, 0);
					ParcoursMain.rightMotor.rotate(100, false);
				} while (res[0] < 0.2);
				//ParcoursMain.leftMotor.rotate(-120, false);
				sensorMover.rotateTo(0, false);
			}
			
			//line is right
			else if (diff > 0.1) {
				ParcoursMain.leftMotor.stop();
				ParcoursMain.rightMotor.stop();
				sensorMover.rotateTo(-60, false);
				do {
					redMode.fetchSample(res, 0);
					ParcoursMain.leftMotor.rotate(30, true);
					ParcoursMain.rightMotor.rotate(-100, false);
				} while (res[0] < 0.2);
				sensorMover.rotateTo(0, false);
			}
			//ParcoursMain.moveTo(null);
		}
	},
	COUNT("Count") {
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
	LINEFOLLOW_REGULATED("Follow line using regulator"){
		private Port colorPort;
		private EV3ColorSensor color;
		private RegulatedMotor sensorMover;
		private SensorMode redMode;
		private final double p = 1;
		private final double offset = 0.5;
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
			ParcoursMain.leftMotor.setSpeed(360 + y);
			ParcoursMain.rightMotor.setSpeed(360 - y);
		}
	};

	public String name;
	ParcoursSegment(String name) {
		this.name = name;
	}
	public abstract void init();
	public abstract void doStep();
}
