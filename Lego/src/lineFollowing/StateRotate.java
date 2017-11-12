package lineFollowing;

import lejos.hardware.ev3.LocalEV3;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.utility.Delay;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateRotate extends State {
	private final int ROTATION_TRY_ANGLE = 25;
	private final int SENSOR_DELAY = 5;

	private int lastRotationPrefix = 1;
	private boolean secondTry = false;
	
	public StateRotate(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	@Override
	void init() {
		
		//pilot.rotate(90);
		pilot.addMoveListener(new MoveListener() {
			
			@Override
			public void moveStopped(Move event, MoveProvider mp) {
				System.out.println("move stopped");
				
			}
			
			@Override
			public void moveStarted(Move event, MoveProvider mp) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	void run() throws PortNotDefinedException {
		Thread turning, lineCheck;

		turning = new Thread() {
			public void run() {
				pilot.rotate(lastRotationPrefix * ROTATION_TRY_ANGLE);         // Try k° in the first direction
				lastRotationPrefix = -lastRotationPrefix;
				pilot.rotate(lastRotationPrefix * (90 + ROTATION_TRY_ANGLE));  // Go back k° to center and try k+60° in the other direction
				lastRotationPrefix = -lastRotationPrefix;
				pilot.rotate(lastRotationPrefix * 180);  // Go back 90° to center and try the remaining 90° in the first direction
				lastRotationPrefix = -lastRotationPrefix;
				pilot.rotate(lastRotationPrefix * 90);   // Go back 90° to center, end thread
				stateMachine.changeState(LineFollowing.FORWARD); // Give up (TODO)
			}
		}

		lineCheck = new Thread() {
			public void run() {
				try {
					while(robot.sensors.getColor() < .4) {
						Delay.msDelay(SENSOR_DELAY);
					}
					turning.interrupt();
					pilot.stop();
					stateMachine.changeState(LineFollowing.FORWARD);
				} catch (PortNotDefinedException e) {
					e.printStackTrace();
				}
			}
		}

		lineCheck.start();
		turning.start();

		try {
			turning.join();
			lineCheck.interrupt();
		} catch (InterruptedException e) { }


		/*for (int degree = 0; degree <= 90; degree += 5) {
			//System.out.println("run: " + (robot.sensors.getColor() > .4));
			if (robot.sensors.getColor() < .4) {
				if (lastRotationLeft) {
					pilot.rotate(5);
				} else {
					pilot.rotate(-5);
				}
				
			} else {
				pilot.stop();
				stateMachine.changeState(LineFollowing.FORWARD);
				return;
			}
		}
		if (lastRotationLeft) {
			pilot.rotate(-90);
		} else {
			pilot.rotate(90);
		}
		if (!secondTry) {
			secondTry = true;
		} else {
			secondTry = false;
			pilot.stop();
			stateMachine.changeState(LineFollowing.FORWARD);
		}
		
		lastRotationLeft = !lastRotationLeft;*/
		
		//pilot.rotate(90);
		/*System.out.println("run: " + (robot.sensors.getColor() > .4));
		if (robot.sensors.getColor() > .4) {
			pilot.stop();
			stateMachine.changeState(new StateForward(stateMachine, pilot, robot));
		}*/
		
	}

	@Override
	void leave() {
		// TODO Auto-generated method stub
		
	}

}
