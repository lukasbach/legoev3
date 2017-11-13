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
	private int angleStep;
	private int direction = 1;
	
	public StateRotate(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
		angleStep = 5;
	}
	
	@Override
	void init() {
		pilot.setRotateSpeed(40);
	}
	
	private boolean checkSensor() throws PortNotDefinedException {
		while(pilot.isMoving()/*pilot.getAngleIncrement() > -85*/) {
			if (robot.sensors.getColor() > .4) {
				//System.out.println("AAA");
				pilot.stop();
				stateMachine.changeState(LineFollowing.FORWARD);
				//System.out.println("BBB");
				return true;
			}	
		}
		return false;
	}

	@Override
	void run() throws PortNotDefinedException {
		/*System.out.println("30° in first direction");
		pilot.rotate(30 * direction, true);
		if (this.checkSensor()) return;
		direction *= -1;

		System.out.println("30° back");
		pilot.setRotateSpeed(500);
		pilot.rotate(30 * direction);
		pilot.setRotateSpeed(40);

		System.out.println("90° in other direction");
		pilot.rotate(90 * direction, true);
		if (this.checkSensor()) return;
		direction *= -1;

		System.out.println("90°+30° back");
		pilot.setRotateSpeed(500);
		pilot.rotate(120 * direction);
		pilot.setRotateSpeed(40);

		System.out.println("60° rest");
		pilot.rotate(60 * direction, true);
		if (this.checkSensor()) return;
		direction *= -1;

		System.out.println("90° back to center");
		pilot.rotate(90 * direction);
		//if (this.checkSensor()) return;
		direction *= -1;*/
		
		//pilot.rotate(80 * direction)
		
		pilot.rotate(90 * direction, true);
		this.checkSensor();
		direction *= -1;
		
		pilot.setRotateSpeed(200);
		pilot.rotate(90 * direction);
		pilot.setRotateSpeed(40);
		
		pilot.rotate(90 * direction, true);
		this.checkSensor();
		direction *= -1;
		
		pilot.rotate(90 * direction);
		StateGap.lastTurn = direction;
		stateMachine.changeState(LineFollowing.GAP);
		
		//stateMachine.changeState(LineFollowing.GAP);
		/*
		for (int degree = angleStep; degree <= 180; degree += angleStep) {
			//System.out.println("run: " + (robot.sensors.getColor() > .4));
			if (robot.sensors.getColor() < .4) {
				if (lastRotationLeft) {
					pilot.rotate(degree);
				} else {
					pilot.rotate(-degree);
				}
				
			} else {
				pilot.stop();
				angleStep = 5;
				stateMachine.changeState(LineFollowing.FORWARD);
				return;
			}
			angleStep += 5;
			lastRotationLeft = !lastRotationLeft;
		}
		angleStep = 5;
		/*if (lastRotationLeft) {
			pilot.rotate(-90);
		} else {
			pilot.rotate(90);
		}*/
	/*	if (!secondTry) {
			secondTry = true;
		} else {
			secondTry = false;
			pilot.stop();
			stateMachine.changeState(LineFollowing.GAP);
		}
		
		//lastRotationLeft = !lastRotationLeft;
		
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
