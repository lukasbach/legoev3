package lineFollowing;

import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateForward extends State {
	
	private static int ACC = 4000;
	private static int SPEED = 200;
	private float intensity = 0;
	 
	public StateForward(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	@Override
	public void run() throws PortNotDefinedException {
		intensity = robot.sensors.getColor();
		
		//sensor sees black
		if (intensity < 0.4f) {
			stateMachine.changeState(LineFollowing.ROTATE);
		}
	}

	@Override
	void init() {
		Sound.beep();
		pilot.setAcceleration(ACC);
		pilot.setTravelSpeed(SPEED);
		pilot.forward();
	}

	@Override
	void leave() {
		pilot.stop();
	}

}
