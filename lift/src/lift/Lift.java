package lift;

public class Lift extends Thread {
	private LiftMonitor monitor;
	private boolean moveUp;
	private int next;
	private LiftView lv;

	public Lift(LiftMonitor monitor, LiftView lv) {
		this.monitor = monitor;
		this.lv = lv;
		moveUp = true;
		next = 0;
	}

	/*
	 * runs the elevator
	 */
	public void run() {
		while (true) {
			int temp = next;
			nextLevel();
			monitor.moveLift(next);
			lv.moveLift(temp, next);
		}
	}
	/*
	 * Generates the next target floor for the elevator
	 */

	private void nextLevel() {
		if (moveUp) {
			next++;
			if (next == 6) {
				moveUp = false;
			}
		} else {
			next--;
			if (next == 0) {
				moveUp = true;
			}
		}
	}

}
