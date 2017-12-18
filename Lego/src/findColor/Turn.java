package findColor;

import java.io.File;

import labyrinth.Labyrinth;
import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lineFollowing.LineFollowing;
import lineFollowing.StateRotate;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;
import robotcontrol.SensorWrapper;

public class Turn extends State {
	
	final static int MOVE_ACCELERATION = 4000;
	final static int MOVE_SPEED = 200;
	final static int STOPPING_ANGLE_EPS = 5;
	private boolean firstTurn;
	private int lastTurnDirection;
	private final int durationDriveBack = 500;
	private final int durationDriveForward = 600;

	public Turn(FindingColor stateMachine, DifferentialPilot pilot, Robot robot)
	{
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
		firstTurn = false;
		lastTurnDirection = -1;
	}
	
	@Override
	public void init() {
		pilot.setAcceleration(MOVE_ACCELERATION);
		pilot.setTravelSpeed(MOVE_SPEED);
		//if (firstTurn) {
			/*pilot.backward();
			Delay.msDelay(durationDriveBack);
			pilot.rotate(90);
			pilot.forward();
			Delay.msDelay(durationDriveForward);
			pilot.rotate(90);*/
		/*} else {
			pilot.backward();
			Delay.msDelay(durationDriveForward);
			pilot.rotate(90);
			pilot.forward();
			Delay.msDelay(durationDriveForward);
			pilot.rotate(90);
		}*/
		
		try {
			pilot.backward();
			Delay.msDelay(durationDriveBack);
			turnAndSearchRelatively(lastTurnDirection * 90);
			pilot.forward();
			Delay.msDelay(durationDriveForward);
			turnAndSearchRelatively(lastTurnDirection * 90);
			lastTurnDirection *= -1;
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			stateMachine.changeState(FindingColor.FIRST_COLOR);	
		}
	}
	
	private boolean turnAndSearchRelatively(int angle) throws PortNotDefinedException {
		pilot.rotate(angle, true); // Make sure to turn AT LEAST angleToTurn °

		while (true) {
			if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
				Sound.playSample(new File("R2D2N1.wav"));
			}
		}
		return true;
	}
	
	private boolean turnAndSearch(int speed, float targetAngle) throws PortNotDefinedException {
		float angleToTurn = Math.abs(targetAngle - robot.sensors.getGyro());
		int direction = targetAngle - this.robot.sensors.getGyro() > 0 ? 1 : -1;
		
		pilot.setRotateSpeed(speed);
		pilot.rotate(direction * (angleToTurn + 30), true); // Make sure to turn AT LEAST angleToTurn °

		while (true) {
			if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
				Sound.playSample(new File("R2D2N1.wav"));
			}

			if (Math.abs(targetAngle - robot.sensors.getGyro()) < STOPPING_ANGLE_EPS) {
				pilot.stop();
				return false;
			}
		}
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
