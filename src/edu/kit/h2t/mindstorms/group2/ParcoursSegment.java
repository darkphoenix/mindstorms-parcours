package edu.kit.h2t.mindstorms.group2;

public enum ParcoursSegment {
	LINEFOLLOW("Follow line") {
		private int cnt;
		public void init() {
			cnt=0;
		}
		public void doStep() {
			cnt++;
			if(cnt>10)
				ParcoursMain.moveTo(LOOP);
		}
	},
	LOOP("Loop forever") {
		public void init() {}
		public void doStep() {}
	};

	public String name;
	ParcoursSegment(String name) {
		this.name = name;
	}
	public abstract void init();
	public abstract void doStep();
}
