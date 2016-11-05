package lift;

public class LiftMonitor {
	private int here, next;
	private int[] waitEntry;
	private int[] waitExit;
	private int load;
	private LiftView view;
	
	public LiftMonitor(LiftView view){
		this.view = view;
		here = 0;
		next = 0;
		waitEntry = new int[7];		//6 våningar
		waitExit = new int[7];		//6 våningar
		load = 0;
	}
	
	/**
	 * Notifies the monitor that a person has entered the floor determined by
	 * floor.
	 * 
	 * @param floor
	 *            , the current floor of the person.
	 */
	private void enterFloor(int floor){
		waitEntry[floor]++;
		view.drawLevel(floor, waitEntry[floor]);
		notifyAll();
	}
	/**
	 * Determines whether a person may enter the elevator or not.
	 * 
	 * @param floor
	 * @return true if a person may enter the elevator. False if not.
	 */
	public boolean canEnter(int floor){
		return load < 4 && next == here && floor == here;
	}
	
	/*
	  * Handles a persons actions from the point of arriving at a floor until
	 * exiting the elevator.
	 * 
	 * @param nextFloor
	 *            , the target floor of the person.
	 * @param currentFloor
	 *            , the current floor of the person. 	
	 */
	public synchronized void liftAction(int nextFloor, int currentFloor){
		enterFloor(currentFloor);
		while(!canEnter(currentFloor)){
			try{
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		load++;
		waitEntry[here]--;
		waitExit[nextFloor]++;
		view.drawLevel(here, waitEntry[here]);
		view.drawLift(here, load);
		notifyAll();
		while(here != nextFloor){
			try{
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		exitLift();
	}
	/*
	 * Removes a person from the lift
	 * Someone exits the lift
	 */
	private void exitLift() {
		waitExit[here]--;
		load--;
		view.drawLift(here, load);
		notifyAll();		
	}
	
	/*
	 * Moves the elevator to the floor given in next.
	 * 
	 * @param next
	 * 					, the upcoming floor of the elevator.
	 */
	public synchronized void moveLift(int next) {
		here = this.next;
		notifyAll();
		while(waitExit[here] > 0 || (waitEntry[here] > 0 && load < 4)){
			try{
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		this.next = next;
		notifyAll();
	}

}
