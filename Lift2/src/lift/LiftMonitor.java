package lift;

public class LiftMonitor extends Thread {
	private int here, next, load;
	private int[] waitEntry; // The number of persons waiting to enter the lift
								// at the various floors
	private int[] waitExit; // The number of persons (inside the lift) waiting
							// to
							// leave the lift at various floors.
	private LiftView view;
	private int sum;
	private boolean goingUp;

	public LiftMonitor(LiftView view) {
		this.view = view;
		here = 0;
		next = 0;
		load = 0;
		waitEntry = new int[7]; // 6 floors
		waitExit = new int[7];
		goingUp = true;
		sum = 0;
	}

	

	/*
	 * Increases the people waiting on the startFloor Draws the startFloor with
	 * the right amount of people.
	 * 
	 * @param startFloor
	 */
	public synchronized void addToEntryQueue(int startFloor) {
		waitEntry[startFloor]++;
		sum++;
		view.drawLevel(startFloor, waitEntry[startFloor]);
		notifyAll();
	}

	/*
	 * Called when a person wants to go into the lift As long as the lift is on
	 * the wrong floor or if it's full Updates waitEntry and waitExit
	 * 
	 * @param startFloor
	 * 
	 * @param endFloor
	 */

	public synchronized void enterLift(int startFloor, int endFloor) {
		while (!(isHere(startFloor) && load < 4 && here == next)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		waitEntry[here]--;
		waitExit[endFloor]++;
		load++;
		view.drawLevel(here, waitEntry[here]);
		view.drawLift(here, load);
		notifyAll();
	}

	/*
	 * Called when a person is in the lift As long as the lift is on the wrong
	 * floor you wait Updates waitEntry and waitExit
	 * 
	 * @param endFloor
	 */
	public synchronized void exitLift(int endFloor) {
		while (!isHere(endFloor)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		waitExit[here]--;
		load--;
		sum--;
		view.drawLift(here, load);
		notifyAll();
	}
	
	/*
	 * Called when the lift is still. It waits until everybody, who wants to,
	 * has left or everybody has gotten on.
	 */
	
	
	/**
	 * Calculates next floor
	 */
	public synchronized void calculateNext() {
		here = next;
		notifyAll();
		//folk vill gå på och den inte är full || folk vill gå av || ingen väntar på någon våning
		while(waitEntry[here] != 0 && load < 4 || waitExit[here] != 0 || (sum == 0)){
			try{
				wait();
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			
		}
		if(goingUp){
			//skulle kunna kolla ifall det finns någon som ska till högre våningar också
			if(next == 6){
				next--;
				goingUp = false;
			}else{
				next++;
			}
		}else{
			if(next==0){
				next++;
				goingUp=true;
			}else{
				next--;
			}
		}
		notifyAll();
	}
	/**
	 * Checks if the lift is here
	 * @param startFloor
	 * @return
	 */
	private boolean isHere(int floor) {
		return here == floor;
	}
	/**
	 * 
	 * @return where the lift is and where it's going
	 */
	public synchronized int[] getFloors(){
		int[] floors = {here, next};
		return floors;
	}
	

}
