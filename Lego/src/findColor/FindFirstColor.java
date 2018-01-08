package findColor;

import java.io.File;

import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;
import robotcontrol.SensorWrapper;

public class FindFirstColor extends State {
	
	final static int MOVE_ACCELERATION = 2000;
	final static int MOVE_SPEED = 100;
	public static boolean foundWhite;
	public static boolean foundRed;

	public FindFirstColor(FindingColor stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
		foundWhite = false;
		foundRed = false;
	}
	
	@Override
	public void init() {
		pilot.setAcceleration(MOVE_ACCELERATION);
		pilot.setTravelSpeed(MOVE_SPEED);
		pilot.forward();
	}

	@Override
	public void run() throws PortNotDefinedException {
		if (robot.sensors.getTouch() != 0) {
			stateMachine.changeState(FindingColor.TURN);
		}
		
		if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_BLUE) {
			stateMachine.changeState(FindingColor.TURN);
			Sound.beepSequence();
		}
		
		FindFirstColor.testColor(robot, pilot);
	}
	
	static public void testColor(Robot robot, DifferentialPilot pilot) throws PortNotDefinedException {		
		if (!FindFirstColor.foundRed && robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
			Sound.beepSequenceUp();
			FindFirstColor.foundRed = true;
			//Sound.playSample(new File("R2D2N1.wav"));
			if (FindFirstColor.foundWhite) {
				pilot.stop();
				Sound.beepSequenceUp();
			}
		}
		else if (!FindFirstColor.foundWhite && robot.sensors.getColor() == SensorWrapper.COLOR_ID_LINE) {
			Sound.beepSequenceUp();
			//Sound.playSample(new File("R2D2N1.wav"));
			FindFirstColor.foundWhite = true;
			if (FindFirstColor.foundRed) {
				pilot.stop();
				Sound.beepSequenceUp();
			}
		}
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub
		
	}

}
