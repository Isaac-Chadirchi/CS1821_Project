package CS1821_Project;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class findGap implements Behavior {
	private MovePilot pilot;
	private EV3TouchSensor touch1;
	private EV3TouchSensor touch2;
	private gapFoundBool gapBool;
	
	findGap(MovePilot p, EV3TouchSensor ts1, EV3TouchSensor ts2, gapFoundBool gapBool) {
		this.pilot = p;
		this.touch1 = ts1;
		this.touch2 = ts2;
		this.gapBool = gapBool;
		this.gapBool.setBool(false);
		touch1.getTouchMode();
		touch2.getTouchMode();
	}
	
	public void action() {
		float[] ts1_sample = new float[1];
		float[] ts2_sample = new float[1];
		touch1.fetchSample(ts1_sample, 0);
		touch2.fetchSample(ts2_sample, 0);
		if(!(pilot.isMoving())) {
			pilot.forward();
		}
	}
	
	public void suppress() {
	}
	
	public boolean takeControl() {
		float[] ts1_sample = new float[1];
		float[] ts2_sample = new float[1];
		touch1.fetchSample(ts1_sample, 0);
		touch2.fetchSample(ts2_sample, 0);
		if((!(ts1_sample[0] == 0 || ts2_sample[0] == 0))&&(!gapBool.getBooel())) {
			return true;
		}else {
			return false;
		}
	}
}
class Allign implements Behavior {
	private MovePilot pilot;
	private EV3TouchSensor touch1;
	private EV3TouchSensor touch2;
	public boolean gap_found;
	private BaseRegulatedMotor mLeft;
	private BaseRegulatedMotor mRight;
	private gapFoundBool gapBool;
	
	Allign(MovePilot p, EV3TouchSensor ts1, EV3TouchSensor ts2, BaseRegulatedMotor left, BaseRegulatedMotor right, gapFoundBool gapBool) {
		this.pilot = p;
		this.touch1 = ts1;
		this.touch2 = ts2;
		this.mLeft = left;
		this.mRight = right;
		this.gapBool = gapBool;
		
		touch1.getTouchMode();
		touch2.getTouchMode();
	}
	
	public void action() {
		/* GAP_FOUND USED TO STOP FIND_GAP BEVAHIOR */
		
		
		/* Touch Sensor Mode (IF sample = 1 -> PRESSED) */
		float[] ts1_sample = new float[1];
		float[] ts2_sample = new float[1];		
		
		touch1.fetchSample(ts1_sample, 0);
		touch2.fetchSample(ts2_sample, 0);
		
		/* ALLIGN IF GAP FOUND ON THE LEFT SIDE */
		if (ts2_sample[0] < 0.05) {
			
			while(ts2_sample[0] < 0.05) {
				touch1.fetchSample(ts1_sample, 0);
				touch2.fetchSample(ts2_sample, 0);
				mLeft.backward();
				mRight.stop();
			}
			
			while(ts1_sample[0] > 0.5){
				touch1.fetchSample(ts1_sample, 0);
				touch2.fetchSample(ts2_sample, 0);
				mRight.forward();
				mLeft.stop();
			}
			
			while(ts2_sample[0] > 0.5){
				touch1.fetchSample(ts1_sample, 0);
				touch2.fetchSample(ts2_sample, 0);
				mLeft.forward();
				mRight.stop();
			}
		}else if (ts1_sample[0] < 0.05) {
			
			/* ALLIGN IF GAP FOUND ON THE RIGHT SIDE */
			
			while(ts1_sample[0] < 0.05) {
				touch1.fetchSample(ts1_sample, 0);
				touch2.fetchSample(ts2_sample, 0);
				mRight.backward();
				mLeft.stop();
			}
			
			while(ts2_sample[0] > 0.5){
				touch1.fetchSample(ts1_sample, 0);
				touch2.fetchSample(ts2_sample, 0);
				mLeft.forward();
				mRight.stop();
			}
			
			while(ts1_sample[0] > 0.5){
				touch1.fetchSample(ts1_sample, 0);
				touch2.fetchSample(ts2_sample, 0);
				mRight.forward();
				mLeft.stop();
			}
		}
		pilot.travel(-50);
		gapBool.setBool(true);
		
	}
	
	public void suppress() {
		mLeft.stop();
		mRight.stop();
	}

	public boolean takeControl() {
		/* TAKE CONTROL IF GAP FOUND */
		
		/* Touch Sensor Mode (IF sample = 1 -> PRESSED) */
		float[] ts1_sample = new float[1];
		float[] ts2_sample = new float[1];
		
		touch1.fetchSample(ts1_sample, 0);
		touch2.fetchSample(ts2_sample, 0);
		
		return((ts1_sample[0] == 0 || ts2_sample[0] == 0)&&(!gapBool.getBooel()));
	}
}
