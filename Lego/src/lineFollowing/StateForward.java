package lineFollowing;

import lejos.hardware.Sound;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateForward extends State {
	
	private static int ACC = 4000;
	private static int SPEED = 200;
	private float intensity = 0;
	private SampleProvider touch;
	private EV3TouchSensor touchSensor;
	 
	public StateForward(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
		touchSensor = new EV3TouchSensor(SensorPort.S2);
		touch = touchSensor.getTouchMode();
	}
	
	@Override
	public void run() throws PortNotDefinedException {
		intensity = robot.sensors.getColor();
		
		if (getTouch()) {
			stateMachine.changeState(LineFollowing.OBSTACLE);
		}
		//sensor sees black
		if (intensity < 0.4f) {
			stateMachine.changeState(LineFollowing.ROTATE);
		}
		
	}
	
	private boolean getTouch() {
		float[] sample = new float[touch.sampleSize()];
		touch.fetchSample(sample, 0);
		return (sample[0] == 0) ? false : true;
	}

	@Override
	public void init() {
		Sound.beep();
		pilot.setAcceleration(ACC);
		pilot.setTravelSpeed(SPEED);
		pilot.forward();
	}

	@Override
	public void leave() {
		pilot.stop();
	}

}
