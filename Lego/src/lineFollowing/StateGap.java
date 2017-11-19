package lineFollowing;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateGap extends State {

	
	public StateGap(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	@Override
	public void run() throws PortNotDefinedException {
		Delay.msDelay(500);
		pilot.stop();
		if (tryFind(1)) return;
		if (tryFind(-1)) return;
		
		pilot.setAcceleration(4000);
		pilot.setTravelSpeed(200);
		pilot.forward();
		Delay.msDelay(1000);
	}

	@Override
	public void init() {
		pilot.setAcceleration(4000);
		pilot.setTravelSpeed(200);
		pilot.forward();
		Delay.msDelay(500);
		
	}
	
	private boolean tryFind(int direction) {
		pilot.rotate(90 * direction, true);
		while (pilot.isMoving()) {
			try {
				if (robot.sensors.getColor() > 0.4f) {
					stateMachine.changeState(LineFollowing.FORWARD);
					return true;
				}
			} catch (PortNotDefinedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pilot.rotate(-90 * direction);
		return false;
	}

	@Override
	public void leave() {
		pilot.stop();
	}


	
}
