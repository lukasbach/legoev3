package bridgeCrossingPID;

import findColor.FindingColor;
import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class BridgeCrossing {

	private Robot robot;
	private DifferentialPilot pilot;

	private static int MOVE_ACCELERATION = 200;
	private static int MOVE_SPEED = 200;
	private static float DISTANCE_FLOOR = 0.144f;

	private float distance = 0;
	private float error = 0;
	private float lastError = 0;
	private float integral = 0;
	private float deriv = 0;
	private double correction = 0;
	private float kP = 3000.0f;
	private float kI = 10.0f;
	private float kD = 0.0f;
	private float leftMotorSpeed = 0;
	private float rightMotorSpeed = 0;

	@SuppressWarnings("deprecation")
	public BridgeCrossing(Robot robot, DifferentialPilot pilot) {
		this.pilot = pilot;
		this.robot = robot;
		try {
			run();
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void run() throws PortNotDefinedException {
		int counter = 0;
		init();
		
		//move up ramp a little bit for pid to work
		pilot.travel(90);
		
		int initialTacho = robot.motors.leftMotor.getTachoCount();
		System.out.println("init: " + initialTacho);
		//drive up ramp 90% with pid
		//while (counter < 220) {
		while(Math.abs(robot.motors.leftMotor.getTachoCount() - initialTacho) < 2000) {
			//System.out.println(Math.abs(robot.motors.leftMotor.getTachoCount() - initialTacho));
			counter ++;
			pidLoop(0.02f, 300, 1, 3000, 10, 0);
		//}
		}
		System.out.println("init: " + initialTacho);
		System.out.println(robot.motors.leftMotor.getTachoCount() - initialTacho);
		Sound.twoBeeps();
		
		//drive over black tape (blind)
		pilot.setTravelSpeed(100);
		pilot.travel(300, false);
		
		
		
		//drive to edge
		pilot.setTravelSpeed(50);
		pilot.travel(1000, true);
		while (robot.sensors.getColors()[0] > 0.01) {	
		}
		//detected edge
		Sound.beepSequenceUp();
		
		//rotate
		pilot.travel(-80);
		pilot.rotate(100);
		
//		int longEdgeCounter = 0;
//		//drive along edge fast (PID)
//		while (longEdgeCounter < 350) {
//			longEdgeCounter++;
//			pidLoop(0.12f, 500, 0.7f, 700, 0, 0);
//		}
		initialTacho = robot.motors.leftMotor.getTachoCount();
		while(Math.abs(robot.motors.leftMotor.getTachoCount() - initialTacho) < 4000) {
			pidLoop(0.12f, 500, 0.7f, 700, 0, 0);
		}
		System.out.println("init: " + initialTacho);
		System.out.println(robot.motors.leftMotor.getTachoCount() - initialTacho);
		
		//drive to edge
		Sound.beepSequence();
		System.out.println("SERACHING FOOR EDGE");
		pilot.travel(600, true);
		while (robot.sensors.getColors()[0] > 0.01) {	
		}
		Sound.beepSequenceUp();
		
		//Turn and Start driving down ramp
		pilot.travel(-130);
		pilot.rotate(97, true);
		robot.motors.headMotor.rotateTo(-230);
		pilot.travel(250);
		
		//Drive down ramp pid
		counter = 0;
		while (counter < 950) {
			counter ++;
			pidLoop(0.04f, 50, -1, 2000, 10, 0);
		}
		
		//Try to drive through hole in wall
		Sound.beepSequence();
		robot.motors.headMotor.rotateTo(0);
		pilot.stop();
		pilot.rotate(-10);
		pilot.travel(400);
		
		new FindingColor(robot, pilot);	
	}
	
	private void pidLoop(float target, float motorSpeed, float correctionMultiplicator, int kP, int kI, int kD) throws PortNotDefinedException {
		distance = robot.sensors.getDistance();
		if (distance > DISTANCE_FLOOR + 0.3)
			distance = DISTANCE_FLOOR + 0.3f;
		
		//Catch false readings
		if (Math.abs(distance) > 1000) return;
		
		//PID
		error = DISTANCE_FLOOR + target - distance;
		integral += error;
		deriv = error - lastError;
		lastError = error;
		correction = kP * error + kI * integral + kD * deriv;
		correction *= correctionMultiplicator;
		leftMotorSpeed = (float) (motorSpeed + correction);
		rightMotorSpeed = (float) (motorSpeed - correction);
		robot.motors.rightMotor.setSpeed(rightMotorSpeed);
		robot.motors.leftMotor.setSpeed(leftMotorSpeed);
		
		if (leftMotorSpeed < 0) {
			robot.motors.leftMotor.forward();
		} else {
			robot.motors.leftMotor.backward();
		}
		if (rightMotorSpeed < 0) {
			robot.motors.rightMotor.forward();
		} else {
			robot.motors.rightMotor.backward();
		}
	}
	
	private void init() throws PortNotDefinedException {
		robot.sensors.gyroReset();
		robot.motors.headMotor.setAcceleration(200);
		robot.motors.headMotor.rotateTo(-115); // wait until arm is on the front right
		
		pilot.setAcceleration(MOVE_ACCELERATION);
		pilot.setTravelSpeed(MOVE_SPEED);robot.sensors.gyroReset();
		robot.motors.headMotor.setAcceleration(200);
		robot.motors.headMotor.rotateTo(-115); // wait until arm is on the front right
		
		pilot.setAcceleration(MOVE_ACCELERATION);
		pilot.setTravelSpeed(MOVE_SPEED);
	}

}
