package labyrinth;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lineFollowing.LineFollowing;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;
import robotcontrol.SensorWrapper;

public class MakeDecision extends State {

	final static int SEARCH_SPEED = 35;
	final static int FAST_SPEED = 80;

	final static int TURN_ANGLE_EXTRA = 15;
	final static int STOPPING_ANGLE_EPS = 3;
	
	@SuppressWarnings("deprecation")
	public MakeDecision(Labyrinth stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	
	@Override
	public void init() {
		
		try {
			//positive degrees = left
			
			//test right
			if (turnAndSearch(SEARCH_SPEED, -90)) {
				stateMachine.changeState(Labyrinth.FOLLOW_LINE);
			}
			pilot.forward();
			Delay.msDelay(20);
			pilot.stop();
			
			//test forward
			if(turnAndSearch(SEARCH_SPEED, -90)) {
				stateMachine.changeState(Labyrinth.FOLLOW_LINE);
			}
			pilot.backward();
			Delay.msDelay(20);
			pilot.stop();
			
			//test left
			if (turnAndSearch(SEARCH_SPEED, -90)) {
				stateMachine.changeState(Labyrinth.FOLLOW_LINE);
			}
			
			//found dead end
			turnAndSearch(FAST_SPEED, 90);
			if(turnAndSearch(SEARCH_SPEED, 180)) {
				stateMachine.changeState(Labyrinth.FOLLOW_LINE);
			}
			
			System.out.println("found no line to follow");
			return;
			
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() throws PortNotDefinedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub

	}
	
	
	private boolean turnAndSearch(int speed, float targetAngle) throws PortNotDefinedException {
		float angleToTurn = Math.abs(targetAngle - this.robot.sensors.getGyro());
		int direction = targetAngle - this.robot.sensors.getGyro() > 0 ? 1 : 0;

		pilot.setRotateSpeed(speed);
		pilot.rotate(direction * (angleToTurn + TURN_ANGLE_EXTRA), true);

		while (true) {
			if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_LINE) {
				pilot.stop();
				stateMachine.changeState(LineFollowing.FORWARD);
				return true;
			}

			if (Math.abs(targetAngle - this.robot.sensors.getGyro()) < STOPPING_ANGLE_EPS) {
				pilot.stop();
				break;
			}
		}

		return false;
	}

}
