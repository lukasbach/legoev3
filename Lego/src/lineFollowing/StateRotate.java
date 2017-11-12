package lineFollowing;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateRotate extends State implements MoveListener {
	private static final int ANGLE_STEP = 5;
	private boolean turning = false;
	private int rotationDirection; //-1 left; 1 right
	
	public StateRotate(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	@Override
	void init() {
		pilot.addMoveListener(this);
	}

	@Override
	void run() throws PortNotDefinedException {

		for (int angle = ANGLE_STEP; angle <= 180; angle += ANGLE_STEP) {
			pilot.rotate(angle * rotationDirection, true);
			rotationDirection *= -1;
			
			while (turning) {
				if (robot.sensors.getColor() > .4) {
					turning = false;
					pilot.stop();
					stateMachine.changeState(LineFollowing.FORWARD);
					return;
				}
			}
		}
		
		//Give up
		
	}

	@Override
	void leave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveStarted(Move event, MoveProvider mp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveStopped(Move event, MoveProvider mp) {
		turning = false;
		
	}

}
