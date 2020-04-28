import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class main_ver2 {
	
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
		MovePilot pilot = new MovePilot(chas);
		
		pilot.setLinearSpeed(50);
		pilot.setAngularSpeed(50);
		motorL.setSpeed (50); 
		motorR.setSpeed (50);

		pilot.travel(50); // Travel forward (50 cm.). 
						 // When the robot stops, it has to be located in front of a gap. 
						 // It will look like the Touch Sensors stopped the robot

		pilot.travel(-5); // Travel Backward before Placing the bridge. (Aligning)

		motorB.setSpeed(10); 
		motorB.forward();	// Place the Bridge.
		Delay.msDelay(200); // Change the value based on the test result.

		pilot.travel(-5); // Prepare to drive over
		motorB.backward();
		Delay.msDelay(200);

		pilot.travel(40); // Drive Over.

		pilot.rotate(180); // Rotate to pick up the bridge.

		bridgeM.forward(); // Prepare to pick up.
		Delay.msDelay(200);
				
		pilot.travel(10); // Pick up bridge.
		bridgeM.backward();
		Delay.msDelay(200);

		pilot.rotate(160); // Robot choosing random direction.
		pilot.travel(100); // Robot continues travelling
	}
}
