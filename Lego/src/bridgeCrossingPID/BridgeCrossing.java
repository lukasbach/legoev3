package bridgeCrossingPID;

import lejos.robotics.navigation.DifferentialPilot;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class BridgeCrossing {
	
	private Robot robot;
	private DifferentialPilot pilot;
	
	private static int MOVE_ACCELERATION = 4000;
	private static int MOVE_SPEED = 200;
	private static float DISTANCE_FLOOR = 0.13f;
	
	private float distance = 0; 
	private float error = 0; 
	private float lastError = 0; 
	private float integral = 0; 
	private float deriv = 0; 
	private double correction = 0; 
	private float kP = 1500.0f; 
	private float kI = 60.0f; 
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
		robot.motors.headMotor.rotateTo(-70); //wait until arm is on the front right
		pilot.setAcceleration(MOVE_ACCELERATION);
		pilot.setTravelSpeed(MOVE_SPEED);
		pilot.travel(500);
		pilot.rotate(-20);
		pilot.forward();
		while (robot.sensors.getDistance() < DISTANCE_FLOOR + 0.05) {
			//wait until robot has moved to the edge
			System.out.println(robot.sensors.getDistance());
		}
		
		while (true) {
			 distance = robot.sensors.getDistance();
			 if (distance > DISTANCE_FLOOR + 0.3) distance = DISTANCE_FLOOR + 0.3f;
			 if (Math.abs(distance) > 1000) continue;
			 error = DISTANCE_FLOOR + 0.2f - distance; 
			 integral += error; 
			 deriv = error - lastError; 
			 lastError = error; 
			 correction = kP * error + kI * integral + kD * deriv;
			 //System.out.println("DISTANCE: " + distance);
			 System.out.println(correction);
			 //System.out.println("error\t" + error);
			 //correction /= 100;
			 leftMotorSpeed = (float) (80.0f + correction);
			 rightMotorSpeed = (float) (80.0f - correction);
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
	}

}
