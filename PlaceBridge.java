package CS1821_Project;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

class PlaceBridge implements Behavior {
	
	private static final int rotatePlaceTime = 200;
	private static final int postRotateMoveTime = -20;
	
	public boolean gapToBig;	
	private MovePilot pilot;
	private BaseRegulatedMotor bridgeM;

	PlaceBridge(MovePilot p, BaseRegulatedMotor bridgeM) {
		this.pilot = p;
		this.bridgeM = bridgeM;
		this.gapToBig = false;
	}

	public void action() {
		pilot.rotate(180);
		bridgeM.setSpeed(10);
		bridgeM.forward();
		Delay.msDelay(rotatePlaceTime);
		pilot.travel(postRotateMoveTime);
		bridgeM.backward();
		Delay.msDelay(rotatePlaceTime);
		this.gapToBig = true;
	}

	public void suppress() {
	}

	public boolean takeControl() {
		return !gapToBig;
	}
}

class GapMeasure implements Behavior {
	
	private MovePilot pilot;
	private EV3TouchSensor touch1;
	private EV3TouchSensor touch2;
	private BaseRegulatedMotor bridgeM;
	private PlaceBridge placer;

	GapMeasure(MovePilot p, BaseRegulatedMotor bridgeM, EV3TouchSensor ts1, EV3TouchSensor ts2, PlaceBridge placerB) {
		this.pilot = p;
		this.bridgeM = bridgeM;
		this.touch1 = ts1;
		this.touch2 = ts2;
		this.placer = placerB;
	}

	public void action() {
		placer.gapToBig = true;
		pilot.stop();
		
		bridgeM.setSpeed(10);
		bridgeM.setStallThreshold(1, 50);
		
		while (!bridgeM.isStalled()) {
			bridgeM.backward();
		}
		
		bridgeM.setStallThreshold(50, 1000);
	}

	public void suppress() {
	}

	public boolean takeControl() {
		/* Touch Sensor Mode (IF sample = 1 -> PRESSED) */
		float[] ts1_sample = new float[1];
		float[] ts2_sample = new float[1];
		
		touch1.fetchSample(ts1_sample, 0);
		touch2.fetchSample(ts2_sample, 0);
		
		return(ts1_sample[0] < 0.1 && ts2_sample[0] < 0.1);
	}
}