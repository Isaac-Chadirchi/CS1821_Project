package CS1821_Project;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class Pickup {
	
	private static final int rotateDistanceTime = 200;

	public static void main(MovePilot pilot, BaseRegulatedMotor bridgeM, EV3ColorSensor cs) {
		
		bridgeM.setSpeed(10);
		
		while (!bridgeM.isStalled()) {
			bridgeM.forward();
		}
		
		pickupAlign(pilot, cs);
		
		bridgeM.backward();
		Delay.msDelay(rotateDistanceTime);
		
		pilot.rotate(180);
	}
	
	private static void pickupAlign(MovePilot pilot, EV3ColorSensor cs) {
		
		
		
	}
}
