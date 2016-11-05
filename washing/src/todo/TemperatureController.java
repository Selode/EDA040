package todo;


import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private double temp;
	private boolean ack, heat;
	private TemperatureEvent tempEvent;

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (10000/speed)); 
		this.mach = mach;
		temp = 0;
		ack = heat = false;
	}

	public void perform() {
		RTEvent rtEvent = mailbox.tryFetch();
		if(rtEvent != null){
			tempEvent = (TemperatureEvent) rtEvent;
			if(tempEvent.getMode() == TemperatureEvent.TEMP_IDLE){
				mach.setHeating(false);
				temp = 0;
				heat = ack = false;
			} else {
				heat = ack = true;
				temp = tempEvent.getTemperature();
				if(mach.getTemperature() < temp && mach.getWaterLevel() > 0){
					mach.setHeating(true);
				}
			}
		}else if(heat && temp > 0 && mach.getTemperature() < temp - 1.8  && mach.getWaterLevel() > 0){
			mach.setHeating(true);
		}else if(heat && temp <= mach.getTemperature()){
			mach.setHeating(false);
			if (ack){
				ack = false;
				((WashingProgram)(tempEvent.getSource())).putEvent(new AckEvent(this));
			}
		}
	}
}
