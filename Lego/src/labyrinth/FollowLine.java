package labyrinth;

import lejos.robotics.navigation.DifferentialPilot;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;
import robotcontrol.SensorWrapper;

public class FollowLine extends State {

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
		try {
			System.out.println("color ID " + this.robot.sensors.getColor());
			//System.out.println("distance: " + this.robot.sensors.getDistance());
		} catch (PortNotDefinedException e) {
			e.printStackTrace();
		}
		if (this.robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
			stateMachine.changeState(Labyrinth.MAKE_DECISION);
		}
		if(this.robot.sensors.getColor() == SensorWrapper.COLOR_ID_GROUND) {
			stateMachine.changeState(Labyrinth.FIND_LINE);
		}
		
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub

	}

}
