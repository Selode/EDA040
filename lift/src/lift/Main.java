package lift;

public class Main {
	public static void main(String [] args) {
		LiftView view = new LiftView();
		LiftMonitor monitor = new LiftMonitor(view);
		Lift lift = new Lift(monitor, view);
		for(int i = 0; i<20;i++){
			Person p = new Person(monitor);
			p.start();
		}
		lift.start();
	}
}
