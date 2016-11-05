package todo;

import done.*;

public class WashingController implements ButtonListener {
	private AbstractWashingMachine theMachine;
	private double theSpeed;
	private TemperatureController tempControl;
	private WaterController waterControl;
	private SpinController spinControl;
	private WashingProgram washProg;

	public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		this.theMachine = theMachine;
		this.theSpeed = theSpeed;
		tempControl = new TemperatureController(theMachine, theSpeed);
		waterControl = new WaterController(theMachine, theSpeed);
		spinControl = new SpinController(theMachine, theSpeed);
		tempControl.start();
		waterControl.start();
		spinControl.start();
	}

	public void processButton(int theButton) {
		switch (theButton) {
		case 1:
			if (washProg == null || !washProg.isAlive()) {
				washProg = new WashingProgram1(theMachine, theSpeed, tempControl, waterControl, spinControl);
				washProg.start();
			}
			break;
		case 2:
			if (washProg == null || !washProg.isAlive()) {
				washProg = new WashingProgram2(theMachine, theSpeed, tempControl, waterControl, spinControl);
				washProg.start();
			}
			break;
		case 3:
			if (washProg == null || !washProg.isAlive()) {
				washProg = new WashingProgram3(theMachine, theSpeed, tempControl, waterControl, spinControl);
				washProg.start();
			}
			break;
		default:
			if (washProg != null && washProg.isAlive()) {
				washProg.interrupt();
			}
		}

	}
}
