package lineFollowing;

import lejos.robotics.navigation.DifferentialPilot;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;


public class LineFollowing {
	public static final int FORWARD = 0;
	public static final int ROTATE = 1;
	public static final int GAP = 2;
	

	private DifferentialPilot pilot;
	private int state;
	private State[] states = new State[3];
	
	public LineFollowing(Robot robot) {
		this.pilot = new DifferentialPilot(30, 160, robot.motors.leftMotor, robot.motors.rightMotor, true);
		state = 0;
		states[0] = new StateForward(this, pilot, robot);
		states[1] = new StateRotate(this, pilot, robot);
		states[2] = new StateGap(this, pilot, robot);
		
		states[state].init();

		while (true) {
			try {
				states[state].run();
			} catch (PortNotDefinedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}
	
	public void changeState(int state) {
		this.states[this.state].leave();
		this.state = state;
		this.states[state].init();
	}


}
