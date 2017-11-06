package lineFollowing;

import lejos.robotics.navigation.DifferentialPilot;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateForward extends State {
	
	private float intensity = 0;
	 
	public StateForward(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	@Override
	public void run() throws PortNotDefinedException {
		intensity = robot.sensors.getColor();
//		try {
//			System.out.println(robot.sensors.getColor());
//		} catch (PortNotDefinedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if (intensity < 0.4f) {
			stateMachine.changeState(LineFollowing.ROTATE);
		}
		
	}

	@Override
	void init() {
		pilot.setAcceleration(4000);
		pilot.setTravelSpeed(200);
		pilot.forward();
		
	}

	@Override
	void leave() {
		pilot.stop();
	}

}
