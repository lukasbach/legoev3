package lineFollowing;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateRotate extends State {
	private static final int ANGLE_STEP = 5;
	private boolean turning = false;
	private int rotationDirection; //-1 left; 1 right
	
	public StateRotate(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	@Override
	void init() {
	}

	
	float color = 0;
	float error = 0;
	float lastError = 0;
	float integral = 0;
	float deriv = 0;
	float correction = 0;
	float kP = 500.0f;
	float kI = 20.0f;
	float kD = 0.0f;
	
	@Override
	void run() throws PortNotDefinedException {
		color = robot.sensors.getColor();
		error = 0.4f - color;
		integral += error;
		deriv = error - lastError;
		lastError = error;
		correction = kP * error + kI * integral + kD * deriv;
		System.out.println(correction);
		
		robot.motors.leftMotor.setSpeed(50 + correction);
		//robot.motors.rightMotor.setSpeed(50 - correction);
		//if (50 - correction <= 0 ) {
		//	robot.motors.rightMotor.setSpeed(0);
		//}
		robot.motors.leftMotor.backward();
		robot.motors.rightMotor.backward();
		
//		if (robot.sensors.getColor() > 0.4) {
//			robot.motors.leftMotor.flt();
//			robot.motors.rightMotor.flt();
//			stateMachine.changeState(LineFollowing.FORWARD);
////
//		}
//		
		
//		System.out.println("test");
//		pilot.stop();
//		stateMachine.changeState(LineFollowing.FORWARD);
//		turning = true;
//		for (int angle = ANGLE_STEP; angle <= 180; angle += ANGLE_STEP) {
//			//.out.println(angle);
//
//			pilot.rotate(angle * rotationDirection, true);
//			rotationDirection *= -1;
//		
////			}
//		}
//		stateMachine.changeState(LineFollowing.FORWARD);
		
		//Give up
		
	}

	@Override
	void leave() {
		// TODO Auto-generated method stub
		
	}



}
