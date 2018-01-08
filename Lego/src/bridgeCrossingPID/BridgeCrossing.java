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
		
		//move up ramp a little bit for pid tró work
		pilot.travel(80);
		
		//drive up ramp 90% with pid
		while (counter < 220) {
			System.out.println(counter);
			counter ++;
			pidLoop(0.02f, 300, 1, 3000, 10, 0);
		}
		
		//drive over black tape (blind)
		pilot.setTravelSpeed(100);
		pilot.travel(300, false);
		
		
		
		//drive to edge
		pilot.setTravelSpeed(50);
		pilot.travel(300, true);
		while (robot.sensors.getColors()[0] > 0.01) {	
		}
		//detected edge
		Sound.beepSequenceUp();
		
		//rotate
		pilot.travel(-80);
		pilot.rotate(100);
		
		int longEdgeCounter = 0;
		//drive along edge fast (PID)
		while (longEdgeCounter < 350) {
			longEdgeCounter++;
			System.out.println(longEdgeCounter);
			pidLoop(0.12f, 500, 0.7f, 700, 0, 0);
		}
		
		//drive to edge
		Sound.beepSequence();
		
//		int longEdgeCounter2 = 0;
//		//drive along edge fast (PID)
//		while (longEdgeCounter2 < 150) {
//			longEdgeCounter2++;
//			System.out.println(longEdgeCounter2);
//			pidLoop(0.12f, 100, 0.7f, 1000, 0, 0);
//		}
		System.out.println("SERACHING FOOR EDGE");
		pilot.travel(600, true);
		while (robot.sensors.getColors()[0] > 0.01) {	
		}
		Sound.beepSequenceUp();
		
		pilot.travel(-160);
		
		
		pilot.rotate(90, true);
		robot.motors.headMotor.rotateTo(-230);
		pilot.travel(250);
		
		counter = 0;
		while (counter < 400) {
			System.out.println(robot.sensors.getDistance());
			counter ++;
			pidLoop(0.04f, 50, -1, 2000, 10, 0);
		}
		Sound.beepSequence();
		robot.motors.headMotor.rotateTo(0);
		pilot.stop();
		pilot.rotate(-15);
		pilot.travel(250);
		
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
