package bridgeCrossingPID;

import lejos.robotics.navigation.DifferentialPilot;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class BridgeCrossing {
	
	private Robot robot;
	private DifferentialPilot pilot;
	
	private static int MOVE_ACCELERATION = 4000;
	private static int MOVE_SPEED = 200;
	private static int DISTANCE_FLOOR = 15;
	
	private float distance = 0; 
	private float error = 0; 
	private float lastError = 0; 
	private float integral = 0; 
	private float deriv = 0; 
	private float correction = 0; 
	private float kP = 500.0f; 
	private float kI = 20.0f; 
	private float kD = 0.0f; 
	
	
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
		robot.motors.headMotor.rotateTo(20); //wait until arm is on the front right
		pilot.setAcceleration(MOVE_ACCELERATION);
		pilot.setTravelSpeed(MOVE_SPEED);
		pilot.rotate(-20);
		pilot.forward();
		while (robot.sensors.getDistance() < DISTANCE_FLOOR) {
			//wait until robot has moved to the edge
		}
		
		while (true) {
			 distance = robot.sensors.getDistance(); 
			 error = DISTANCE_FLOOR - distance; 
			 integral += error; 
			 deriv = error - lastError; 
			 lastError = error; 
			 correction = kP * error + kI * integral + kD * deriv; 
			 System.out.println(correction); 
			
			 robot.motors.rightMotor.setSpeed(50 - correction);
			 robot.motors.leftMotor.setSpeed(50 + correction); 
			    //robot.motors.rightMotor.setSpeed(50 - correction); 
			    //if (50 - correction <= 0 ) { 
			    //  robot.motors.rightMotor.setSpeed(0); 
			    //} 
			 robot.motors.leftMotor.backward(); 
			 robot.motors.rightMotor.backward();
		}
	}

}
