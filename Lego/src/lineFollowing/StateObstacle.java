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
	
	public StateObstacle(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
	}
	
	private void drive(int timeMS) {
		pilot.setTravelSpeed(SPEED);
		pilot.setAcceleration(ACC);
		pilot.forward();
		Delay.msDelay(timeMS);
		pilot.stop();
	}
	
	private float getAngle() {
		try {
			return this.robot.sensors.getGyro();
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	private void turn(int targetAngle) {
		float angleToTurn = Math.abs(targetAngle - getAngle());
		if (angleToTurn < 1) return;
		
		pilot.rotate(targetAngle);
		/*while(true) {
			if (Math.abs(targetAngle - getAngle()) < 5) {
				pilot.stop();
				break;
			}
		}*/
	}
	
	@Override
	public void init() {
		try {
			this.robot.sensors.gyroReset();
		} catch (PortNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		turn(-90);
		drive(850);
		turn(90);
		drive(1750);
		turn(90);
		drive(850);
		turn(-90);
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
