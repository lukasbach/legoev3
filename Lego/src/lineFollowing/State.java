package lineFollowing;

import lejos.robotics.navigation.DifferentialPilot;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public abstract class State {
	protected LineFollowing stateMachine;
	protected DifferentialPilot pilot;
	protected Robot robot;
	
	abstract void init();
	abstract void run() throws PortNotDefinedException;
	abstract void leave();
	
}
