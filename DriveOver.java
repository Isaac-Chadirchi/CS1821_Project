package  CS1821_Project;


import lejos.hardware.Button;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

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
		float maxLight, minLight, avgLight;
		maxLight = minLight = 0;
		float[] samples1 = new float[1];
		SampleProvider sp = cs.getRedMode();
		
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
		}
		
		}
	
	
	public void suppress() {
	}
	
	public boolean takeControl() {
		return true;
	}
	}



