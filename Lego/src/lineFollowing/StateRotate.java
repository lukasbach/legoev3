package lineFollowing;

import labyrinth.Labyrinth;
import lejos.robotics.navigation.DifferentialPilot;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;
import robotcontrol.SensorWrapper;

public class StateRotate extends State {

	final static int SEARCH_SPEED = 43;
	final static int FAST_SPEED = 90;

	final static int TURN_ANGLE_EXTRA = 15;
	final static int STOPPING_ANGLE_EPS = 3;

	// TODO: Move to robot
	private boolean lastRotationLeft = true;
	private float rotationDirection = 1;

	@SuppressWarnings("deprecation")
	StateRotate(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}

	@Override
	public void init() {
		// Resetting Gyro angles. Robot needs to be stationary for that.
		pilot.quickStop();
		try {
			robot.sensors.gyroReset();
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() throws PortNotDefinedException {

		if (turnAndSearch(SEARCH_SPEED, rotationDirection * 30)) return;
		rotationDirection *= -1;
		if (turnAndSearch(FAST_SPEED, 0)) return;
		
		if (turnAndSearch(SEARCH_SPEED, rotationDirection * 30)) return;
		rotationDirection *= -1;
		if (turnAndSearch(FAST_SPEED, 30)) return;

		if (turnAndSearch(SEARCH_SPEED, rotationDirection * 90)) return;
		rotationDirection *= -1;
		if (turnAndSearch(FAST_SPEED, 30)) return;
		
		if (turnAndSearch(SEARCH_SPEED, rotationDirection * 90)) return;
		rotationDirection *= -1;
		if (turnAndSearch(FAST_SPEED, 0)) return;

		stateMachine.changeState(LineFollowing.GAP);
	}

	@Override
	public void leave() {
	}

	private boolean turnAndSearch(int speed, float targetAngle) throws PortNotDefinedException {
		float angleToTurn = Math.abs(targetAngle - robot.sensors.getGyro());
		int direction = targetAngle - this.robot.sensors.getGyro() > 0 ? 1 : -1;
		
		pilot.setRotateSpeed(speed);
		pilot.rotate(direction * (angleToTurn + 30), true); // Make sure to turn AT LEAST angleToTurn �

		while (true) {
			if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_LINE) {
				pilot.quickStop();
				stateMachine.changeState(LineFollowing.FORWARD);
				return true;
			} else if (this.robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
				// Go to labyrinth
				stateMachine.stop();
				new Labyrinth(robot, pilot);
				return true;
			} 

			if (Math.abs(targetAngle - robot.sensors.getGyro()) < STOPPING_ANGLE_EPS) {
				pilot.quickStop();
				return false;
			}
		}
	}
}
