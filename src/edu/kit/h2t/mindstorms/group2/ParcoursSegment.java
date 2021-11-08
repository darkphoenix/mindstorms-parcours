package edu.kit.h2t.mindstorms.group2;

import lejos.utility.Delay;

public enum ParcoursSegment {
	LINEFOLLOW("Follow line") {
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
