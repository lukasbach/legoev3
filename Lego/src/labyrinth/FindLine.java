package labyrinth;

import lejos.robotics.navigation.DifferentialPilot;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;
import robotcontrol.SensorWrapper;

public class FindLine extends State {
	final static int SEARCH_SPEED = 35;
	final static int FAST_SPEED = 80;

	final static int TURN_ANGLE_EXTRA = 15;
	final static int STOPPING_ANGLE_EPS = 3;

	// TODO: Move to robot
	private boolean lastRotationLeft = true;
	private float rotationDirection = 1;

	@SuppressWarnings("deprecation")
	FindLine(Labyrinth stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}

	@Override
	public void init() {
		System.out.println("find line");
		// Resetting Gyro angles. Robot needs to be stationary for that.
		pilot.stop();
		try {
			robot.sensors.gyroReset();
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() throws PortNotDefinedException {

		if (turnAndSearch(SEARCH_SPEED, -90)) return;
		
		if (turnAndSearch(FAST_SPEED, 0)) return;

		if (turnAndSearch(SEARCH_SPEED, 90)) return;
		
		if (turnAndSearch(FAST_SPEED, 0)) return;

	}

	@Override
	public void leave() {
	}

	private boolean turnAndSearch(int speed, float targetAngle) throws PortNotDefinedException {
		float angleToTurn = Math.abs(targetAngle - this.robot.sensors.getGyro());
		int direction = targetAngle - this.robot.sensors.getGyro() > 0 ? 1 : -1;

		pilot.setRotateSpeed(speed);
		pilot.rotate(direction * (angleToTurn + TURN_ANGLE_EXTRA), true);

		while (true) {
			if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_LINE) {
				pilot.stop();
				stateMachine.changeState(Labyrinth.FOLLOW_LINE);
				return true;
			}
			if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
				pilot.stop();
				stateMachine.changeState(Labyrinth.MAKE_DECISION);
			}

			if (Math.abs(targetAngle - this.robot.sensors.getGyro()) < STOPPING_ANGLE_EPS) {
				pilot.stop();
				break;
			}
		}

		return false;
	}
}


