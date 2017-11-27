package labyrinth;

import lejos.hardware.Sound;
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
	
	private boolean right;
	private boolean left;
	private boolean forward; 
	
	@SuppressWarnings("deprecation")
	public MakeDecision(Labyrinth stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
		
		right = false;
		left = false;
		forward = false;
	}
	
	
	@Override
	public void init() {
		
		try {
			robot.sensors.gyroReset();
			//positive degrees = left
			System.out.println("make decision");
			pilot.travel(50);
			Sound.beepSequenceUp();
			//test right
			
			right = turnAndSearch(SEARCH_SPEED, -90);
			if (right) {
				Sound.beepSequenceUp();
				stateMachine.changeState(Labyrinth.FOLLOW_LINE);
				return;
			}
			
			forward = turnAndSearch(SEARCH_SPEED, 0);
			//test forward
			if(forward) {
				stateMachine.changeState(Labyrinth.FOLLOW_LINE);
				return;
			}
			
			//test left
			left = turnAndSearch(SEARCH_SPEED, 90);
			if (left) {
				stateMachine.changeState(Labyrinth.FOLLOW_LINE);
				return;
			}
			
			//found dead end
			turnAndSearch(FAST_SPEED, 0);
			if(turnAndSearch(SEARCH_SPEED, 180)) {
				stateMachine.changeState(Labyrinth.FOLLOW_LINE);
				return;
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
				//stateMachine.changeState(Labyrinth.FORWARD);
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
