package edu.kit.h2t.mindstorms.group2;

import lejos.hardware.Button;
import lejos.hardware.Sound;
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
		segments.put("Exit", null);
		segments.put(KoopLineFollow.class.getSimpleName(), new KoopLineFollow());
		segments.put(Hermes.class.getSimpleName(), new Hermes());
		segments.put(FindBridge.class.getSimpleName(), new FindBridge());
		segments.put(Bridge.class.getSimpleName(), new Bridge());
		segments.put("Bridge 2", new Bridge(2));
		segments.put(Maze.class.getSimpleName(), new Maze());
		
		String names[] = segments.keySet().toArray(new String[segments.size()]);
		
		while(true) {
			TextMenu segmentMenu = new TextMenu(names, 1, "Segment");
			int segNum = segmentMenu.select();
			if(segNum == -1) continue;
			if(names[segNum] == "Exit") break;
			moveTo(names[segNum]);
			while(RobotUtil.chk())
				seg.doStep();
			//Stop, in case segment didn't end properly
			RobotUtil.sensorMover.stop();
			RobotUtil.sensorMoverCenter();
			RobotUtil.syncStop();
			RobotUtil.lcd.clear();
			RobotUtil.abort(false);
		}
	}
	
	public static void moveTo(String segNew) {
		Sound.beep();
		seg = segments.get(segNew);
		RobotUtil.lcd.clear();
		RobotUtil.lcd.drawString(segNew, 1, 1);
		seg.init();
	}
}