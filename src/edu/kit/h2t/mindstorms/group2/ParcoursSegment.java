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
			float[] leftSample = new float[1];
			float[] rightSample = new float[1];
			
			sensorMover.rotateTo(-120, false);
			redMode.fetchSample(leftSample, 0);
			
			sensorMover.rotateTo(0, false);
			redMode.fetchSample(rightSample, 0);
			
			float leftVal = leftSample[0];
			float rightVal = rightSample[0];
			float diff = leftVal - rightVal;
			
			ParcoursMain.lcd.drawString(Float.toString(diff), 0, 5);
			
			//links negativ
			if (diff < -0.1) {
				ParcoursMain.leftMotor.stop();
				ParcoursMain.rightMotor.stop();
				float res[] = new float[1];
				do {
					redMode.fetchSample(res, 0);
					ParcoursMain.rightMotor.rotate(450, false);
				} while (res[0] > 0.4);
				//ParcoursMain.leftMotor.rotate(-120, false);
			}
			
			//recht positiv
			else if (diff > 0.1) {
				ParcoursMain.leftMotor.stop();
				ParcoursMain.rightMotor.stop();
				sensorMover.rotateTo(-120, false);
				float res[] = new float[1];
				do {
					redMode.fetchSample(res, 0);
					ParcoursMain.leftMotor.rotate(100, true);
					ParcoursMain.rightMotor.rotate(-750, false);
				} while (res[0] > 0.4);
				sensorMover.rotateTo(0, false);
			}
			else {
				ParcoursMain.leftMotor.forward();
				ParcoursMain.rightMotor.forward();
			}
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
	};

	public String name;
	ParcoursSegment(String name) {
		this.name = name;
	}
	public abstract void init();
	public abstract void doStep();
}
