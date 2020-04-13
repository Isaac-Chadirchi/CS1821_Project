package  CS1821_Project;


import lejos.hardware.Button;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class DriveOver implements Behavior{
	

	private EV3ColorSensor cs;
	private BaseRegulatedMotor mRight;
	private BaseRegulatedMotor mLeft;
	
	DriveOver(EV3ColorSensor cs, BaseRegulatedMotor left, BaseRegulatedMotor right) {
			this.cs = cs;
			this.mLeft = left;
			this.mRight = right;
	}

	public void action() {
		SampleProvider sp = cs.getRedMode();
		float maxLight, minLight, avgLight;
		maxLight = 0;
		minLight = 0.25f;
		float[] samples1 = new float[1];


		sp.fetchSample(samples1, 0); //GET MAX LIGHT BEFORE CROSSING THE BRIDGE
		maxLight = samples1[0];
		
		avgLight = (minLight + maxLight) / 2;
		
		// GET ON THE BRIDGE
		mLeft . synchronizeWith (new BaseRegulatedMotor [] { mRight });
		mLeft . startSynchronization ();
		mLeft . forward ();
		mRight . forward ();
		mLeft . endSynchronization ();
		Delay . msDelay (200);
		mLeft . stop ();
		mRight . stop ();
		sp.fetchSample(samples1, 0);
		
		//CROSS THE BRIDGE
		while(samples1[0] != (maxLight)) {
			sp.fetchSample(samples1, 0);
			
			if(samples1[0] > avgLight) {
				mRight.stop();
				mLeft.forward();
			}
			if (samples1[0] < avgLight) {
				mRight.forward();
				mLeft.stop();
			}
		}		



		
		/*
		//Light Levels Loop
		while (!Button.ENTER.isDown()) {
			sp.fetchSample(samples1, 0);
			
			if(maxLight < samples1[0]) {
				maxLight = samples1[0];
			}
			if (minLight > samples1[0]) {
				minLight = samples1[0];
			}	
		}
		
		avgLight = (minLight + maxLight) / 2;
		
		
		//Follow-Line Loop
		while(!Button.ENTER.isDown()) {
			sp.fetchSample(samples1, 0);
			
			if(samples1[0] > avgLight) {
				mRight.stop();
				mLeft.forward();
			}
			if (samples1[0] < avgLight) {
				mRight.forward();
				mLeft.stop();
			}
		}*/		
	}
	
	
	public void suppress() {
	}
	
	public boolean takeControl() {
		return true;
	}
	}
