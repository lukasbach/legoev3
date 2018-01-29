package labyrinth;

import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lineFollowing.LineFollowing;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;
import robotcontrol.SensorWrapper;

public class FindBeginning extends State {

	final static int MOVE_ACCELERATION = 4000;
	final static int MOVE_SPEED = 150;
	private long timeTaken;
	private final static int STOPPING_ANGLE_EPS = 10;
	private boolean foundBlue;
	
	int currentSpeed;
	
	public FindBeginning(Labyrinth stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
		this.foundBlue = false;
	}

	@Override
	public void init() {
		currentSpeed = MOVE_SPEED;
		pilot.setAcceleration(MOVE_ACCELERATION);
		pilot.setTravelSpeed(currentSpeed);
		try {
			robot.sensors.gyroReset();
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pilot.forward();
		timeTaken = System.currentTimeMillis();
		
	}

	@Override
	public void run() throws PortNotDefinedException {
		/*if (foundBlue && System.currentTimeMillis() - timeTaken > 2500) {
			pilot.stop();
			turnAndSearch(55, -150);
			pilot.forward();
			Delay.msDelay(1000);
			turnAndSearch(55, 0);
		}*/
		
		if(robot.sensors.getColor() == SensorWrapper.COLOR_ID_BLUE) {
		/*	pilot.stop();
			pilot.setAcceleration(MOVE_ACCELERATION);
			pilot.setTravelSpeed(MOVE_SPEED);
			pilot.forward();
			Delay.msDelay(100);*/
			currentSpeed /= 3;
			this.foundBlue = true;
			//pilot.stop();
			pilot.setTravelSpeed(currentSpeed);
			//pilot.forward();
		}
		if (this.foundBlue && (robot.sensors.getColor() == SensorWrapper.COLOR_ID_LINE || robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED)) {
			pilot.quickStop();
			stateMachine.changeState(Labyrinth.FOLLOW_LINE);
		}
		if (this.foundBlue && robot.sensors.getTouch() > 0) {
			pilot.travel(-250);
			pilot.rotate(-120);
			pilot.forward();
		}
	}
	
	private boolean turnAndSearch(int speed, float targetAngle) throws PortNotDefinedException {
		float angleToTurn = Math.abs(targetAngle - robot.sensors.getGyro());
		int direction = targetAngle - this.robot.sensors.getGyro() > 0 ? 1 : -1;
		
		pilot.setRotateSpeed(speed);
		pilot.rotate(direction * (angleToTurn + 30), true); // Make sure to turn AT LEAST angleToTurn °

		while (true) {
			if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_LINE || robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
				pilot.stop();
				stateMachine.changeState(Labyrinth.FOLLOW_LINE);
				return true;
			} 
			if (Math.abs(targetAngle - robot.sensors.getGyro()) < STOPPING_ANGLE_EPS) {
				pilot.stop();
				return false;
			}
		}
	}


	@Override
	public void leave() {
		// TODO Auto-generated method stub
	}

}