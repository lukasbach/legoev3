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
		
	}

	@Override
	public void run() throws PortNotDefinedException {
		try {
			//System.out.println("color0:"  + this.robot.sensors.getColors()[0]);
			//System.out.println("color1:"  + this.robot.sensors.getColors()[1]);
			//System.out.println("color2:"  + this.robot.sensors.getColors()[2]);
			System.out.println("color ID " + this.robot.sensors.getColors()[0]);
			System.out.println("distance: " + this.robot.sensors.getDistance());
			} catch (PortNotDefinedException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub
		
	}

}
