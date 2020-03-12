package CS1821_Project;

import java.util.Random;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

class Backup implements Behavior {
	
	private MovePilot turner;
	private Random rgen = new Random();
	private boolean Backedup = false;

	Backup(MovePilot p) {
		this.turner = p;
	}

	public void action() {
		turner.travel(-50);
		turner.rotate((2 * rgen.nextInt(2) - 1) * 30);
		Backedup = true;
	}

	public void suppress() {
	}

	public boolean takeControl() {
		return (allinged || bridgePlaced);
	}
}