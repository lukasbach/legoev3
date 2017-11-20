package lineFollowing;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateObstacle extends State {
	private static int ACC = 4000;
	private static int SPEED = 200;

	public StateObstacle(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	private void drive(int timeMS) {
		pilot.setTravelSpeed(SPEED);
		pilot.setAcceleration(ACC);
		pilot.forward();
		Delay.msDelay(timeMS);
		pilot.stop();
	}
	
	@Override
	public void init() {
		pilot.rotate(90);
		drive(200);
		pilot.rotate(-90);
		drive(1000);
		pilot.rotate(-90);
		drive(200);
		pilot.rotate(90);
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
