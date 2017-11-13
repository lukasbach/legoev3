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
	private final int SENSOR_DELAY = 3;

	private int lastRotationPrefix = 1;
	private boolean secondTry = false;
	
	public StateRotate(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	void init() {
		
		//pilot.rotate(90);
		pilot.addMoveListener(new MoveListener() {
			
			@Override
			public void moveStopped(Move event, MoveProvider mp) {
				
			}
			
			@Override
			public void moveStarted(Move event, MoveProvider mp) {
				// TODO Auto-generated method stub
				
			}
		});


	}

	@Override
	void run() throws PortNotDefinedException {
		
		final Thread turning;
		Thread lineCheck;

		turning = new Thread() {
			public void run() {
				pilot.rotate(lastRotationPrefix * ROTATION_TRY_ANGLE);         // Try k° in the first direction
				if(isInterrupted()) return;
				lastRotationPrefix = -lastRotationPrefix;
				//System.out.print("1");
				pilot.rotate(lastRotationPrefix * (90 + ROTATION_TRY_ANGLE));  // Go back k° to center and try k+60° in the other direction
				if(isInterrupted()) return;
				lastRotationPrefix = -lastRotationPrefix;
				//System.out.print("2");
				pilot.rotate(lastRotationPrefix * 180);  // Go back 90° to center and try the remaining 90° in the first direction
				if(isInterrupted()) return;
				lastRotationPrefix = -lastRotationPrefix;
				//System.out.print("3");
				pilot.rotate(lastRotationPrefix * 90);   // Go back 90° to center, end thread
				if(isInterrupted()) return;
				//stateMachine.changeState(LineFollowing.FORWARD); // Give up (TODO)
				//System.out.print("done\n");
			}
		};

		lineCheck = new Thread() {
			public void run() {
				try {
					while(robot.sensors.getColor() < .4) {
						//Delay.msDelay(SENSOR_DELAY);
						//if (isInterrupted()) return;
					}
					System.out.print("found");
					turning.interrupt();
					//pilot.stop();
					//stateMachine.changeState(LineFollowing.FORWARD);
				} catch (PortNotDefinedException e) {
					e.printStackTrace();
				}
			}
		};

		lineCheck.start();
		turning.start();

		try {
			turning.join();
			lineCheck.interrupt();
			pilot.stop();
			//System.out.println("interrupted");
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
		stateMachine.changeState(LineFollowing.FORWARD);

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
