package labyrinth;

import lejos.robotics.navigation.DifferentialPilot;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class FollowLine extends State {

	public FollowLine(Labyrinth stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	@Override
	public void init() {
		try {
		System.out.println("color:"  + this.robot.sensors.getSamples());
		} catch (PortNotDefinedException e) {
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

}
