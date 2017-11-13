package lineFollowing;

import lejos.hardware.ev3.LocalEV3;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateRotate extends State {

	private boolean lastRotationLeft = false;
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
		for (int degree = 0; degree <= 90; degree += 5) {
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
		
		lastRotationLeft = !lastRotationLeft;
		
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
