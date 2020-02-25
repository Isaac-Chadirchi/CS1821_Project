package CS1821_Project;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class asd {
	
	//Sensor Ports
	private static EV3ColorSensor cS = new EV3ColorSensor(SensorPort.S1);
	private static EV3TouchSensor touchLeft = new EV3TouchSensor(SensorPort.S2);
	private static EV3TouchSensor touchRight = new EV3TouchSensor(SensorPort.S3);
	
	//Motor Ports
	private static BaseRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
	private static BaseRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.B);
	private static BaseRegulatedMotor motorUSS = new EV3LargeRegulatedMotor(MotorPort.C);
	private static BaseRegulatedMotor motorBridge = new EV3LargeRegulatedMotor(MotorPort.D);
	
	//Robot Size perams
	final static double offset = 123;
	final static double wheelDiam = 456;
	
	public static void main(String[] args) {
		
		//Pilot Consturction
		Wheel wLeft = WheeledChassis.modelWheel(motorL, wheelDiam).offset(-1 * offset);
		Wheel wRight = WheeledChassis.modelWheel(motorR, wheelDiam).offset(offset);
		Chassis chas = new WheeledChassis((new Wheel[] { wRight, wLeft }), WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot movePilot = new MovePilot(chas);
		
		movePilot.setLinearSpeed(200);
		
		//Behaviour for finding gap
		Behavior trundle = new Trundle(movePilot);
		Behavior gapSense = new GapSense(movePilot, //Touch sensors);
		Arbitrator ab = new Arbitrator(new Behavior[] {trundle, gapSense});
		ab.go();
	}
	
	private static MovePilot getPilot(Port left, Port right, int wDiam, int offset) {
		BaseRegulatedMotor mL = new EV3LargeRegulatedMotor(left);
		Wheel wLeft = WheeledChassis.modelWheel(mL, wDiam).offset(-1 * offset);
		BaseRegulatedMotor mR = new EV3LargeRegulatedMotor(right);
		Wheel wRight = WheeledChassis.modelWheel(mR, wDiam).offset(offset);
		Chassis c = new WheeledChassis((new Wheel[] { wRight, wLeft }), WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot plt = new MovePilot(c);
		return (plt);
	}

}

class Trundle implements Behavior {
	private MovePilot pilot;

	Trundle(MovePilot p) {
		this.pilot = p;
	}

	public void action() {
		if (!(pilot.isMoving())) {
			pilot.forward();
		}
	}

	public void suppress() {
	}

	public boolean takeControl() {
		return true;
	}
}


//Needs full rewrite
class GapSense implements Behavior {
	private MovePilot turner;
	private EV3UltrasonicSensor ultSS;
	private SampleProvider sp = ultSS.getDistanceMode();
	private BaseRegulatedMotor motorUltraSS;
	
	private float[] samples = new float[1];

	GapSense(MovePilot p, EV3UltrasonicSensor ultraSS, BaseRegulatedMotor motorUSS) {
		this.turner = p;
		this.ultSS = ultraSS;
		this.motorUltraSS = motorUSS;
	}

	public void action() {
		
	}

	public void suppress() {
	}

	public boolean takeControl() {
		sp.fetchSample(samples, 0);
		return (samples[0] > 0.1f);
	}
}

