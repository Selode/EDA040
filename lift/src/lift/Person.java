package lift;

public class Person extends Thread {
	private int currentFloor;
	private int targetFloor;
	private LiftMonitor monitor;

	public Person(LiftMonitor monitor) {
		this.monitor = monitor;
		currentFloor = (int) (Math.random() * 7); // Randomizes a floor for
													// initial spawn.
		targetFloor = nextFloor();
	}

	/*
	 * Enables the person to take the elevator, and regenerates the person after
	 * a random amount of time (0-45s) after he/she has exited.
	 */
	public void run(){
		while(true){
			monitor.liftAction(targetFloor, currentFloor);
			try{
				sleep(1000*((int) (Math.random()*46)));
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			currentFloor = targetFloor;
			targetFloor = nextFloor();
		}
	}
	
	/**
	 * Generates a new target floor for the person
	 * 
	 * @return n, the next target floor
	 */
	private int nextFloor() {
		int n = (int) (Math.random()*7);
		while(n==currentFloor){
			n = (int) (Math.random()*7);
		}
		return n;
	}
}
