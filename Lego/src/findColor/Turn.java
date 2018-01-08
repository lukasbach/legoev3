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
	
	final static int MOVE_ACCELERATION = 2000;
	final static int MOVE_SPEED = 100;
	final static int STOPPING_ANGLE_EPS = 3;
	private boolean firstTurn;
	private int lastTurnDirection;
	private int currentRotation;
	private final int durationDriveBack = 300;
	private final int durationDriveForward = 500;

	public Turn(FindingColor stateMachine, DifferentialPilot pilot, Robot robot)
	{
		/*try {
			robot.sensors.gyroReset();
			currentRotation = 0;
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
		firstTurn = false;
		lastTurnDirection = 1;
	}
	
	@Override
	public void init() {
		// Resetting Gyro angles. Robot needs to be stationary for that.
		pilot.stop();
		try {
			robot.sensors.gyroReset();
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pilot.setAcceleration(MOVE_ACCELERATION);
		pilot.setTravelSpeed(MOVE_SPEED);
		
		try {
			pilot.backward();
			Delay.msDelay(durationDriveBack);
			turnAndSearch2(lastTurnDirection * 90);
			pilot.forward();
			Delay.msDelay(durationDriveForward);
			turnAndSearch2(lastTurnDirection * 180);
			//pilot.arc(60 * lastTurnDirection, 180 * lastTurnDirection);
			lastTurnDirection *= -1;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			stateMachine.changeState(FindingColor.FIRST_COLOR);	
		}
	}
	
	private boolean turnAndSearchRelatively(int angle) throws PortNotDefinedException {
		pilot.rotate(angle, true); // Make sure to turn AT LEAST angleToTurn °

		while (pilot.isMoving()) {
			if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
				Sound.beepSequenceUp();
				Sound.playSample(new File("R2D2N1.wav"));
			}
		}
		return true;
	}
	
	private boolean turnAndSearch2(float targetAngle) throws PortNotDefinedException {
		float angleToTurn = Math.abs(targetAngle - robot.sensors.getGyro());
		int direction = targetAngle - this.robot.sensors.getGyro() > 0 ? 1 : -1;
		
		pilot.setRotateSpeed(40);
		pilot.rotate(direction * (angleToTurn + STOPPING_ANGLE_EPS), true); // Make sure to turn AT LEAST angleToTurn °

		while (true) {
			if (Math.abs(targetAngle - robot.sensors.getGyro()) < STOPPING_ANGLE_EPS) {
				pilot.stop();
				return false;
			}

			FindFirstColor.testColor(robot, pilot);
		}
	}

	
	private boolean turnAndSearch(int angle) throws PortNotDefinedException {
		pilot.setRotateSpeed(MOVE_SPEED / 2);
		pilot.rotate(angle + Math.signum(angle) * 40, true); // Make sure to turn AT LEAST angleToTurn °
		
		//currentRotation += angle;
		//currentRotation = currentRotation % 360;
		currentRotation = (int) ((this.robot.sensors.getGyro() + angle) % 360);
		
		while (true) {
			System.out.println("" + Math.floor(robot.sensors.getGyro() % 360) + ", " + Math.floor(currentRotation));
			
			if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
				Sound.playSample(new File("R2D2N1.wav"));
				Sound.beepSequenceUp();
				FindFirstColor.foundRed = true;
				if (FindFirstColor.foundWhite)  {
					pilot.stop();
					Sound.beepSequenceUp();
				}
				return true;
			}
			if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_LINE) {
				Sound.playSample(new File("R2D2N1.wav"));
				FindFirstColor.foundWhite = true;
				Sound.beepSequenceUp();
				if (FindFirstColor.foundRed){
					pilot.stop();
					Sound.beepSequenceUp();
				}
				return true;
			}

			if (Math.abs(currentRotation - (robot.sensors.getGyro() % 360)) < STOPPING_ANGLE_EPS) {
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
