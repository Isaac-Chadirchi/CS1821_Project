package CS1821_Project;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class findGap implements Behavior {
	private MovePilot pilot;
	private boolean gap_found;
	
	findGap(MovePilot p, boolean gap_found) {
		this.pilot = p;
		this.gap_found = gap_found;
	}
	
	public void action() {
		gap_found = Allign.gap_found;
		if((!(pilot.isMoving())) && gap_found == false) {
			pilot.forward();
		}
	}
	
	public void suppress() {
	}
	
	public boolean takeControl() {
		return true;
	}
}
class Allign implements Behavior {
	private MovePilot pilot;
	private EV3TouchSensor touch1;
	private EV3TouchSensor touch2;
	public static boolean gap_found;
	private BaseRegulatedMotor mLeft;
	private BaseRegulatedMotor mRight;
	
	Allign(MovePilot p, EV3TouchSensor ts1, EV3TouchSensor ts2, boolean gap_found, BaseRegulatedMotor left, BaseRegulatedMotor right) {
		this.pilot = p;
		this.touch1 = ts1;
		this.touch2 = ts2;	
		this.gap_found = gap_found;
		this.mLeft = left;
		this.mRight = right;
	}
	
	public void action() {
		/* GAP_FOUND USED TO STOP FIND_GAP BEVAHIOR */
		gap_found = true;
		/* Touch Sensor Mode (IF sample = 1 -> PRESSED) */
		touch1.getTouchMode();
		float[] ts1_sample = new float[1];

		touch2.getTouchMode();
		float[] ts2_sample = new float[1];		
		
		touch1.fetchSample(ts1_sample, 0);
		touch2.fetchSample(ts2_sample, 0);
		
		/* ALLIGN IF GAP FOUND ON THE LEFT SIDE */
		if (ts2_sample[0] < 0.05) {
			
			while(ts2_sample[0] < 0.05) {
				touch2.fetchSample(ts2_sample, 0);
				mLeft.backward();
			}
			
			while(ts1_sample[0] > 0.5){
				touch1.fetchSample(ts1_sample, 0);
				mRight.forward();
			}
			
			while(ts2_sample[0] > 0.5){
				touch2.fetchSample(ts2_sample, 0);
				mLeft.forward();
			}
			
			/* MOVE BACKWARD AFTER ALLIGNING */
			pilot.backward();
			Delay.msDelay(500);
		}
		
		
		/* ALLIGN IF GAP FOUND ON THE RIGHT SIDE */
		if (ts1_sample[0] < 0.05) {
			
			while(ts1_sample[0] < 0.05) {
				touch1.fetchSample(ts1_sample, 0);
				mRight.backward();
			}
			
			while(ts2_sample[0] > 0.5){
				touch2.fetchSample(ts2_sample, 0);
				mLeft.forward();
			}
			
			while(ts1_sample[0] > 0.5){
				touch1.fetchSample(ts1_sample, 0);
				mRight.forward();
			}
			
			/* MOVE BACKWARD AFTER ALLIGNING */
			pilot.backward();
			Delay.msDelay(500);
		}
	}
	
	public void suppress() {
	}

	public boolean takeControl() {
		/* TAKE CONTROL IF GAP FOUND */
		
		/* Touch Sensor Mode (IF sample = 1 -> PRESSED) */
		touch1.getTouchMode();
		float[] ts1_sample = new float[1];

		touch2.getTouchMode();
		float[] ts2_sample = new float[1];
		
		touch1.fetchSample(ts1_sample, 0);
		touch1.fetchSample(ts2_sample, 0);
		
		return(ts1_sample[0] < 0.1 || ts2_sample[0] < 0.1);
	}
}
