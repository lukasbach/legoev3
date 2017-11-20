package lineFollowing;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import main.State;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateObstacle extends State {
	private static int ACC = 4000;
	private static int SPEED = 200;
	//TODO: move to robot
	private EV3GyroSensor gyro;
	private final SampleProvider sp;
	
	public StateObstacle(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
		gyro = new EV3GyroSensor(SensorPort.S4);
		sp = gyro.getAngleMode();
	}
	
	private void drive(int timeMS) {
		pilot.setTravelSpeed(SPEED);
		pilot.setAcceleration(ACC);
		pilot.forward();
		Delay.msDelay(timeMS);
		pilot.stop();
	}
	
	private float getAngle() {
		float[] sample = new float[sp.sampleSize()];
		sp.fetchSample(sample, 0);
		return sample[0];
	}
	
	private void turn(int targetAngle) {
		float angleToTurn = Math.abs(targetAngle - getAngle());
		if (angleToTurn < 1) return;
		
		pilot.rotate(targetAngle, true);
		while(true) {
		if (Math.abs(targetAngle - getAngle()) < 1) {
			pilot.stop();
			break;
			}
		}
	}
	
	@Override
	public void init() {
		gyro.reset();
		turn(-95);
		drive(800);
		turn(95);
		drive(2000);
		turn(95);
		drive(800);
		turn(-95);
		stateMachine.changeState(LineFollowing.ROTATE);
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
