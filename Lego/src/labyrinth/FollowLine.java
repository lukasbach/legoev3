package labyrinth;

import lejos.robotics.navigation.DifferentialPilot;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;
import robotcontrol.SensorWrapper;

public class FollowLine extends State {
	
	final static int MOVE_ACCELERATION = 4000;
	final static int MOVE_SPEED = 100;

	@SuppressWarnings("deprecation")
	public FollowLine(Labyrinth stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}

	@Override
	public void init() {
		System.out.println("follow line");
		pilot.setAcceleration(MOVE_ACCELERATION);
		pilot.setTravelSpeed(MOVE_SPEED);
		pilot.forward();
		
	}

	@Override
	public void run() throws PortNotDefinedException {
		
		if (this.robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
			pilot.stop();
			stateMachine.changeState(Labyrinth.MAKE_DECISION);
		}
		if(this.robot.sensors.getColor() == SensorWrapper.COLOR_ID_GROUND) {
			pilot.stop();
			stateMachine.changeState(Labyrinth.FIND_LINE);
		}
		
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub

	}

}
