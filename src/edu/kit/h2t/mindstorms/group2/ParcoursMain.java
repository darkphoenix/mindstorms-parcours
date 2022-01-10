package edu.kit.h2t.mindstorms.group2;

import lejos.hardware.Button;
import lejos.utility.TextMenu;

import java.util.HashMap;
import java.util.Map;

import edu.kit.h2t.mindstorms.group2.segments.*;

public class ParcoursMain {
	private static ParcoursSegment seg;
	private static Map<String, ParcoursSegment> segments;
	
	public static void main(String[] args) {
		RobotUtil.init();

		//Import modules
		segments = new HashMap<String, ParcoursSegment>();
		segments.put(KoopLineFollow.class.getSimpleName(), new KoopLineFollow());
		segments.put(Hermes.class.getSimpleName(), new Hermes());
		segments.put(FindBridge.class.getSimpleName(), new FindBridge());
		segments.put(Bridge.class.getSimpleName(), new Bridge());
		
		String names[] = segments.keySet().toArray(new String[segments.size()]);
		
		while(!Button.ESCAPE.isDown()) {
			TextMenu segmentMenu = new TextMenu(names, 1, "Segment");
			int segNum = segmentMenu.select();
			moveTo(names[segNum]);
			while(!Button.ESCAPE.isDown())
				seg.doStep();
		}
	}
	
	public static void moveTo(String segNew) {
		seg = segments.get(segNew);
		RobotUtil.lcd.clear();
		RobotUtil.lcd.drawString(segNew, 1, 1);
		seg.init();
	}
}