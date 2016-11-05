package lift;

public class Lift extends Thread{
	private LiftMonitor monitor;
	private LiftView view;
	
	public Lift(LiftMonitor monitor, LiftView view){
		this.monitor = monitor;
		this.view = view;
	}
	
	public void run(){
		while(true){
			moveLift();
		}
	}

	private void moveLift() {
		//monitor.waitForPassengers();
		monitor.calculateNext();
		int[] floors = monitor.getFloors();
		view.moveLift(floors[0], floors[1]);;
		
	}
	

}
