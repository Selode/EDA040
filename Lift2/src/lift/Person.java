package lift;

public class Person extends Thread {
	private int startFloor, endFloor, waitTime;
	private LiftMonitor monitor;
	
	public Person(LiftMonitor monitor){
		this.monitor = monitor;
	}
	/**
	 * A person starts from a random floor and ends on separate random floor
	 * Waits a random time before it queues. The person gets in line, gets on and off the lift 
	 */
	public void run(){
		while(true){
			waitTime = 1000*((int)(Math.random()*46));
			try{
				sleep(waitTime);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			startFloor = (int)(Math.random()*7);
			do{
				endFloor = (int)(Math.random()*7);
			}while(endFloor == startFloor);
			
			monitor.addToEntryQueue(startFloor);
			monitor.enterLift(startFloor, endFloor);
			monitor.exitLift(endFloor);
		}
	}
}
