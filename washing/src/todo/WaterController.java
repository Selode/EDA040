package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;
import se.lth.cs.realtime.event.RTEvent;


public class WaterController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private double waterLevel;
	private boolean ack, fill;
	private WaterEvent wEvent;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
		this.mach = mach;
		ack = false;
		fill = false;
		waterLevel = 0;
	}

	public void perform() {
		RTEvent rtEvent = mailbox.tryFetch();
		if(rtEvent != null){
			wEvent = (WaterEvent) rtEvent;
			int mode = wEvent.getMode();
			if(mode == WaterEvent.WATER_IDLE){
				mach.setDrain(false);
				mach.setFill(false);
				fill = false;
			}else if(mode == WaterEvent.WATER_FILL){
				mach.setDrain(false);
				ack = true;
				fill = true;
				waterLevel = wEvent.getLevel();
				if (mach.getWaterLevel() < wEvent.getLevel()){
					mach.setFill(true);
				}
			}else {
				fill = false;
				mach.setFill(false);
				mach.setDrain(true);
				waterLevel = 0;
				ack = true;
			}
		}else if(fill){
			if(mach.getWaterLevel() >= waterLevel){
				mach.setFill(false);
				if(ack){
					ack = false;
					((WashingProgram) (wEvent.getSource())).putEvent(new AckEvent(this));
				}
			}
			
		}else if(!fill){
			if(mach.getWaterLevel() <= waterLevel){
				mach.setDrain(false);
				if(ack){
					ack = false;
					((WashingProgram) (wEvent.getSource())).putEvent(new AckEvent(this));
				}
			}
		}
	}
}
