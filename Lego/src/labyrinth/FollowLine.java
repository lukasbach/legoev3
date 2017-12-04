package labyrinth;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class FollowLine extends State {
	
	float color = 0;
	float error = 0;
	float lastError = 0;
	float integral = 0;
	float deriv = 0;
	float correction = 0;
	float kP = 1500.0f;
	float kI = 20.0f;
	float kD = 0.0f;
	
	@SuppressWarnings("deprecation")
	public FollowLine(Labyrinth stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}

	@Override
	public void init() {

	}
	
	@Override
	public void run() throws PortNotDefinedException {
		float[] rgb = new float[3];
		((EV3ColorSensor) (robot.sensors.colorSensor.sensor)).getRGBMode().fetchSample(rgb, 0);
		
		System.out.println(rgb[0] + "," + rgb[1] + "," + rgb[2]);

		
		if (false) { // if blue detected
			
		} else {
			error = 0.07f - rgb[0]; //TODO
			integral += error;
			deriv = error - lastError;
			lastError = error;
			correction = kP * error + kI * integral + kD * deriv;
			System.out.println(correction);
			
			 float leftMotorSpeed = (float) (80.0f + correction);
			 float rightMotorSpeed = (float) (80.0f - correction);
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
			 
			 Delay.msDelay(10);
		}
		
	}
		

	@Override
	public void leave() {
		// TODO Auto-generated method stub

	}

}
