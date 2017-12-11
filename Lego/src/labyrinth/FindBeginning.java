package labyrinth;

import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;
import robotcontrol.SensorWrapper;

public class FindBeginning extends State {

	final static int MOVE_ACCELERATION = 4000;
	final static int MOVE_SPEED = 200;
	
	public FindBeginning(Labyrinth stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}

	@Override
	public void init() {
		pilot.setAcceleration(MOVE_ACCELERATION);
		pilot.setTravelSpeed(MOVE_SPEED);
		pilot.forward();
	}

	@Override
	public void run() throws PortNotDefinedException {
		if(robot.sensors.getColor() == SensorWrapper.COLOR_ID_BLUE) {
		/*	pilot.stop();
			pilot.setAcceleration(MOVE_ACCELERATION);
			pilot.setTravelSpeed(MOVE_SPEED);
			pilot.forward();
			Delay.msDelay(100);*/
			Sound.twoBeeps();
		}
		if (robot.sensors.getColor() == SensorWrapper.COLOR_ID_LINE) {
			pilot.stop();
			Sound.beepSequenceUp();
			stateMachine.changeState(Labyrinth.FOLLOW_LINE);
		}
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub
	}

}