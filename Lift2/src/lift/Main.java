package lift;

public class Main {

	public static void main(String[] args) {
		LiftView view = new LiftView();
		LiftMonitor monitor = new LiftMonitor(view);
		Lift lift = new Lift(monitor, view);
		lift.start();
		for(int i = 0; i<20; i++){
			Person person = new Person(monitor);
			person.start();
		}
		

	}

}
