package lineFollowing;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateRotate extends State {

	private static int SEARCH_SPEED = 35;
	private static int FAST_SPEED = 80;

	// TODO: Move to robot
	private boolean lastRotationLeft = true;

	public StateRotate(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}

	@Override
	public void init() {

		// Resetting Gyro angles. Robot needs to be stationary for that.
		pilot.stop();
		try {
			robot.sensors.gyroReset();
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * private boolean checkSensor() throws PortNotDefinedException {
	 * while(pilot.isMoving()) {
	 * 
	 * //found line if (robot.sensors.getColor() > .4) { pilot.stop();
	 * stateMachine.changeState(LineFollowing.FORWARD); return true; } } return
	 * false; }
	 */

	private float getAngle() throws PortNotDefinedException {
		return this.robot.sensors.getGyro();
	}

	private boolean turnAndSearch(int speed, float targetAngle) throws PortNotDefinedException {
		if (targetAngle < -80) {
			lastRotationLeft = false;
		} else if (targetAngle > 80) {
			lastRotationLeft = true; 
		}
		System.out.println(lastRotationLeft);
		float angleToTurn = Math.abs(targetAngle - getAngle());
		
		
		//if (angleToTurn < 1) {
		//	return false;
		//}
		pilot.setRotateSpeed(speed);

		int direction = 1;
		//TODO: maybe switch values
		if (targetAngle - getAngle() > 0) {
			direction = 1;
		} else {
			direction = -1;
		}

		pilot.rotate(direction * (angleToTurn + 30), true); // Make sure to turn AT LEAST angleToTurn �

		while (true) {
			//System.out.println("getAngle:" + getAngle() + "");
			// Proportional turning speed (no overshooting)
			//pilot.setRotateSpeed(targetAngle - getAngle());

			if (robot.sensors.getColor() > .4) {
				pilot.stop();
				stateMachine.changeState(LineFollowing.FORWARD);
				return true;
			}

			if (Math.abs(targetAngle - getAngle()) < 3) {
				pilot.stop();
				break;
			}
		}
		
		return false;
	}

	@Override
	public void run() throws PortNotDefinedException {
		
		if (lastRotationLeft) {
			if (turnAndSearch(SEARCH_SPEED, 90)) return;
			if (turnAndSearch(FAST_SPEED, 0)) return;
			if (turnAndSearch(SEARCH_SPEED, -90)) return;
			if (turnAndSearch(FAST_SPEED, 0)) return;
		} else {
			if (turnAndSearch(SEARCH_SPEED, -90)) return;
			if (turnAndSearch(FAST_SPEED, 0)) return;
			if (turnAndSearch(SEARCH_SPEED, 90)) return;
			if (turnAndSearch(FAST_SPEED, 0)) return;
		}
		
		
		// StateGap.lastTurn = direction;
		stateMachine.changeState(LineFollowing.GAP);
	}

	@Override
	public void leave() {
	}

	/*
	 * pilot.setRotateSpeed(SEARCH_SPEED); pilot.rotate(90 * direction, true);
	 * this.checkSensor();
	 * 
	 * direction *= -1; pilot.setRotateSpeed(FAST_SPEED); pilot.rotate(90 *
	 * direction);
	 * 
	 * pilot.setRotateSpeed(SEARCH_SPEED); pilot.rotate(90 * direction, true);
	 * this.checkSensor();
	 * 
	 * direction *= -1; pilot.setRotateSpeed(FAST_SPEED); pilot.rotate(90 *
	 * direction);
	 * 
	 * 
	 * StateGap.lastTurn = direction; stateMachine.changeState(LineFollowing.GAP);
	 * 
	 */

}
