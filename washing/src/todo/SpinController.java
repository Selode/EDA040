package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;

public class SpinController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private int lastSpinDir;
	private long time;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed)); // TODO: replace with suitable period
		this.mach = mach;
		lastSpinDir = 0;
		time = 0;
	}

	public void perform() {
		time++;
		RTEvent rtEvent = mailbox.tryFetch();
		if (rtEvent != null) {
			int mode = ((SpinEvent) rtEvent).getMode();
			if (mode == SpinEvent.SPIN_OFF) {
				mach.setSpin(AbstractWashingMachine.SPIN_OFF);
				lastSpinDir = AbstractWashingMachine.SPIN_OFF;
			} else if (mode == SpinEvent.SPIN_FAST) {
				mach.setSpin(AbstractWashingMachine.SPIN_FAST);
				lastSpinDir = AbstractWashingMachine.SPIN_FAST;
			} else {
				mach.setSpin(AbstractWashingMachine.SPIN_LEFT);
				lastSpinDir = AbstractWashingMachine.SPIN_LEFT;
				time = 1;
			}
		} else if ((lastSpinDir == AbstractWashingMachine.SPIN_LEFT || lastSpinDir == AbstractWashingMachine.SPIN_RIGHT)
				&& time >= 60) {
			lastSpinDir++;
			if (lastSpinDir == AbstractWashingMachine.SPIN_FAST) {
				lastSpinDir = AbstractWashingMachine.SPIN_LEFT;
			}
			mach.setSpin(lastSpinDir);
			time = 0;
		}
	}
}
