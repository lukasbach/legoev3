package lineFollowing;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateRotate extends State {

	private static int SEARCH_SPEED = 40;
	private static int FAST_SPEED = 200;

	// TODO: Move to robot
	private EV3GyroSensor gyro;

	private final SampleProvider sp;

	public StateRotate(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
		gyro = new EV3GyroSensor(SensorPort.S4);
		sp = gyro.getAngleMode();
	}

	@Override
	public void init() {

		// Resetting Gyro angles. Robot needs to be stationary for that.
		pilot.stop();
		gyro.reset();
	}

	/*
	 * private boolean checkSensor() throws PortNotDefinedException {
	 * while(pilot.isMoving()) {
	 * 
	 * //found line if (robot.sensors.getColor() > .4) { pilot.stop();
	 * stateMachine.changeState(LineFollowing.FORWARD); return true; } } return
	 * false; }
	 */

	private float getAngle() {
		float[] sample = new float[sp.sampleSize()];
		sp.fetchSample(sample, 0);
		return sample[0];
	}

	private boolean turnAndSearch(int speed, float targetAngle) throws PortNotDefinedException {
		float angleToTurn = Math.abs(targetAngle - getAngle());
		
		
		if (angleToTurn < 1) {
			return false;
		}
		pilot.setRotateSpeed(speed);

		int direction = 1;
		//TODO: maybe switch values
		if (getAngle() - targetAngle < 0) {
			direction = 1;
		} else {
			direction = -1;
		}

		pilot.rotate(direction * (angleToTurn + 30), true); // Make sure to turn AT LEAST angleToTurn °

		while (true) {
			System.out.println("getAngle:" + getAngle() + "");
			// Proportional turning speed (no overshooting)
			// pilot.setRotateSpeed(targetAngle - getAngle());

			if (robot.sensors.getColor() > .4) {
				pilot.stop();
				stateMachine.changeState(LineFollowing.FORWARD);
				return true;
			}

			if (Math.abs(targetAngle - getAngle()) < 1) {
				pilot.stop();
				break;
			}
		}
		
		return false;
	}

	@Override
	public void run() throws PortNotDefinedException {

		if (turnAndSearch(SEARCH_SPEED, 90)) return;
		if (turnAndSearch(FAST_SPEED, 0)) return;
		if (turnAndSearch(SEARCH_SPEED, -90)) return;
		if (turnAndSearch(FAST_SPEED, 0)) return;
		
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
