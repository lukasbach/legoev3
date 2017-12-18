package findColor;

import java.io.File;

import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;
import robotcontrol.SensorWrapper;

public class FindFirstColor extends State {
	
	final static int MOVE_ACCELERATION = 4000;
	final static int MOVE_SPEED = 200;
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
		if (!foundRed && robot.sensors.getColor() == SensorWrapper.COLOR_ID_RED) {
			Sound.beepSequenceUp();
			foundRed = true;
			//Sound.playSample(new File("R2D2N1.wav"));
			if (foundWhite) {
				pilot.stop();
				Sound.beepSequenceUp();
			}
		}
		if (!foundWhite && robot.sensors.getColor() == SensorWrapper.COLOR_ID_LINE) {
			Sound.beepSequenceUp();
			//Sound.playSample(new File("R2D2N1.wav"));
			foundWhite = true;
			if (foundRed) {
				pilot.stop();
				Sound.beepSequenceUp();
			}
		}
		
		if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_BLUE) {
			stateMachine.changeState(FindingColor.TURN);
		}
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub
		
	}

}
