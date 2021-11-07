package edu.kit.h2t.mindstorms.group2;

import lejos.utility.TextMenu;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

public class ParcoursMain {
	private static ParcoursSegment seg;
	private static EV3 brick;
	private static TextLCD lcd;
	
	public static void main(String[] args) {
		brick = (EV3) BrickFinder.getLocal();
		lcd = brick.getTextLCD();
		
		String names[] = new String[ParcoursSegment.values().length];
		for(ParcoursSegment s : ParcoursSegment.values()) {
			names[s.ordinal()] = s.name;
		}
		
		while(!Button.ENTER.isDown()) {
			TextMenu segmentMenu = new TextMenu(names, 1, "Segment");
			int segNum = segmentMenu.select();
			moveTo(ParcoursSegment.values()[segNum]);
			while(!Button.ENTER.isDown())
				seg.doStep();
		}
	}
	
	public static void moveTo(ParcoursSegment segNew) {
		seg = segNew;
		lcd.drawString(seg.name, 4, 4);
		seg.init();
	}
}