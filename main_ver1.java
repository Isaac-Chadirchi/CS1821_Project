package CS1821_Project;

import lejos.hardware.Button;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class main_ver1 {
	
	//Sensor Ports
	private static EV3ColorSensor cS = new EV3ColorSensor(SensorPort.S3);
	private static EV3TouchSensor ts1 = new EV3TouchSensor(SensorPort.S1);
	private static EV3TouchSensor ts2 = new EV3TouchSensor(SensorPort.S2);
	
	//Motor Ports
	private static BaseRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
	private static BaseRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.B);
	private static BaseRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.C);
	
	//Robot Size perams (mm)
	final static double offset = 146/2;
	final static double wheelDiam = 41;
	
	public static void main(String[] args) {
		
		//Pilot Consturction
		Wheel wLeft = WheeledChassis.modelWheel(motorL, wheelDiam).offset(-1 * offset);
		Wheel wRight = WheeledChassis.modelWheel(motorR, wheelDiam).offset(offset);
		Chassis chas = new WheeledChassis((new Wheel[] { wRight, wLeft }), WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot movePilot = new MovePilot(chas);
		
		movePilot.setLinearSpeed(50);
		movePilot.setAngularSpeed(50);
		
		motorL.setSpeed (50); 
		motorR.setSpeed (50);
				
		
		gapFoundBool gapBool = new gapFoundBool();
		//Behaviour for finding gap
		findGap gapFinder = new findGap(movePilot, ts1, ts2,  gapBool);
		Allign alligner = new Allign(movePilot, ts1, ts2, motorL, motorR, gapBool);
		EStop stopper = new EStop(movePilot);
		DriveOver driveoverbridge = new DriveOver(cS, motorL, motorR);
		Backup backup = new Backup(movePilot);
		PlaceBridge placer = new PlaceBridge(movePilot, motorB);
		GapMeasure gapMeasurer = new GapMeasure(movePilot, motorB, ts1, ts2, placer);
		
		//Gap finder Arbitrator
		Arbitrator ab1 = new Arbitrator(new Behavior[] {alligner,  gapFinder, stopper }, true);
		ab1.go();
		ab1.stop();
		
		movePilot.travel(-50);
		
		//placer Arbitrator
		Arbitrator ab2 = new Arbitrator(new Behavior[] {stopper, gapMeasurer, placer }, true);
		ab2.go();
		ab2.stop();
		
		//align and drive over Arbitrator
		Arbitrator ab3 = new Arbitrator(new Behavior[] {stopper, driveoverbridge }, true);
		ab3.go();
		ab3.stop();
		
		Pickup.main(movePilot, motorB, cS);
	}
}
class EStop implements Behavior {
	private MovePilot pilot;

	EStop(MovePilot p) {
		this.pilot = p;
	}

	public void action() {
		pilot.stop();
	}

	public void suppress() {
	}

	public boolean takeControl() {
		if (Button.ESCAPE.isDown() ) {
			System.exit(0);
			return true;
		} else {
			return false;
		}
	}
}
