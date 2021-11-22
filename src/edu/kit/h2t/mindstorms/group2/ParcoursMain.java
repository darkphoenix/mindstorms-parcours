package edu.kit.h2t.mindstorms.group2;

import lejos.utility.TextMenu;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class ParcoursMain {
	private static ParcoursSegment seg;
	public static EV3 brick;
	public static TextLCD lcd;
	public static RegulatedMotor leftMotor;
	public static RegulatedMotor rightMotor;
	
	public static void main(String[] args) {
		brick = (EV3) BrickFinder.getLocal();
		lcd = brick.getTextLCD();
		
		leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		rightMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		
		String names[] = new String[ParcoursSegment.values().length];
		for(ParcoursSegment s : ParcoursSegment.values()) {
			names[s.ordinal()] = s.name;
		}
		
		while(!Button.ESCAPE.isDown()) {
			TextMenu segmentMenu = new TextMenu(names, 1, "Segment");
			int segNum = segmentMenu.select();
			moveTo(ParcoursSegment.values()[segNum]);
			while(!Button.ESCAPE.isDown())
				seg.doStep();
		}
	}
	
	public static void moveTo(ParcoursSegment segNew) {
		seg = segNew;
		lcd.clear();
		lcd.drawString(seg.name, 1, 1);
		seg.init();
	}
}