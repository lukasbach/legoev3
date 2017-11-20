package bridgeCrossing;

import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lineFollowing.LineFollowing;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateDriveUp extends State {

	private float intensity = 0;
	
	public StateDriveUp(BridgeCrossing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() throws PortNotDefinedException {
		intensity = robot.sensors.getColor();
		
		//sensor sees black
		if (intensity > 0.4f) {
			Sound.beepSequence();
		}
		
	}
	
	@Override
	public void leave() {
		// TODO Auto-generated method stub
		
	}

}
