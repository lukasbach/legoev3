package lineFollowing;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateGap extends State {

	private float intensity = 0;
	 
	public StateGap(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	@Override
	public void run() throws PortNotDefinedException {
		intensity = robot.sensors.getColor();
		if (intensity > 0.4f) {
			stateMachine.changeState(LineFollowing.FORWARD);
		}
		
	}

	@Override
	void init() {
		pilot.setAcceleration(4000);
		pilot.setTravelSpeed(200);
		pilot.forward();
		Delay.msDelay(2500);
		
	}

	@Override
	void leave() {
		pilot.stop();
	}


	
}
