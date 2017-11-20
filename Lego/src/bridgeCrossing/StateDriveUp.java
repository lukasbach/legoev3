package bridgeCrossing;

import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lineFollowing.LineFollowing;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateDriveUp extends State {

	private static int ACC = 4000;
	private static int SPEED = 200;
	private float intensity = 0;
	
	public StateDriveUp(BridgeCrossing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}

	@Override
	public void init() {
		Sound.twoBeeps();
		Sound.twoBeeps();
		Sound.twoBeeps();
		Sound.twoBeeps();
		Sound.twoBeeps();
		pilot.setAcceleration(ACC);
		pilot.setTravelSpeed(SPEED);
		pilot.forward();		
	}

	@Override
	public void run() throws PortNotDefinedException {
		intensity = robot.sensors.getColor();
		
		//sensor sees black
		if (intensity > 0.6f) {
			Sound.beepSequence();
		}
		
	}
	
	@Override
	public void leave() {
		pilot.stop();
	}

}
