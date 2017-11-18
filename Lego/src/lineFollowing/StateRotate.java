package lineFollowing;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import robotcontrol.PortNotDefinedException;
import robotcontrol.Robot;

public class StateRotate extends State {

	private static int SEARCH_SPEED = 40;
	private static int FAST_SPEED = 200;
	private int direction = 1;
	
	//TODO: Move to robot
	private EV3GyroSensor gyro;
	
	private final SampleProvider sp;
	private float angle = 0;
	
	
	public StateRotate(LineFollowing stateMachine, DifferentialPilot pilot, Robot robot) {
		this.stateMachine = stateMachine;
		this.pilot = pilot;
		this.robot = robot;
		gyro = new EV3GyroSensor(SensorPort.S2);
		sp = gyro.getAngleAndRateMode();
	}
	
	@Override
	void init() {
		
		//Resetting Gyro angles. Robot needs to be stationary for it.
		pilot.stop();
		gyro.reset();
	}
	
	/*private boolean checkSensor() throws PortNotDefinedException {
		while(pilot.isMoving()) {
			
			//found line
			if (robot.sensors.getColor() > .4) {
				pilot.stop();
				stateMachine.changeState(LineFollowing.FORWARD);
				return true;
			}	
		}
		return false;
	}*/

	@Override
	void run() throws PortNotDefinedException {
		
		pilot.setRotateSpeed(SEARCH_SPEED);
		pilot.rotate(120 * direction, true); //Make sure to turn AT LEAST 90°
		while (true) {
			float [] sample = new float[sp.sampleSize()];
	        sp.fetchSample(sample, 0);
	        angle = sample[0];
	        
	        //Proportional turning speed (no overshooting)
	        //pilot.setRotateSpeed(angle);
	        
	        if (robot.sensors.getColor() > .4) {
				pilot.stop();
				stateMachine.changeState(LineFollowing.FORWARD);
			}
	        
	        //TODO: could be -90
	        if (angle >= 90) {
	        	pilot.stop();
	        	break;
	        }	        		
		}
		
		direction *= -1;
		pilot.setRotateSpeed(FAST_SPEED);
		pilot.rotate(120 * direction, true); //Make sure to turn AT LEAST 90°
		while (true) {
			float [] sample = new float[sp.sampleSize()];
	        sp.fetchSample(sample, 0);
	        angle = sample[0];
	        
	        //Proportional turning speed (no overshooting)
	        //pilot.setRotateSpeed(angle);
	        
	        if (angle <= 0) {
	        	pilot.stop();
	        	break;
	        }	        		
		}
		
		pilot.setRotateSpeed(SEARCH_SPEED);
		pilot.rotate(120 * direction, true); //Make sure to turn AT LEAST 90°
		while (true) {
			float [] sample = new float[sp.sampleSize()];
	        sp.fetchSample(sample, 0);
	        angle = sample[0];
	        
	        //Proportional turning speed (no overshooting)
	        //pilot.setRotateSpeed(angle);
	        
	        if (robot.sensors.getColor() > .4) {
				pilot.stop();
				stateMachine.changeState(LineFollowing.FORWARD);
			}
	        
	        if (angle <= -90) {
	        	pilot.stop();
	        	break;
	        }	        		
		}
		
		direction *= -1;
		pilot.setRotateSpeed(FAST_SPEED);
		pilot.rotate(120 * direction, true); //Make sure to turn AT LEAST 90°
		while (true) {
			float [] sample = new float[sp.sampleSize()];
	        sp.fetchSample(sample, 0);
	        angle = sample[0];
	        
	        //Proportional turning speed (no overshooting)
	        //pilot.setRotateSpeed(angle);
	        
	        if (angle >= 0) {
	        	pilot.stop();
	        	break;
	        }	        		
		}
		
		
		//StateGap.lastTurn = direction;
		stateMachine.changeState(LineFollowing.GAP);
		
	}

	@Override
	void leave() {
		// TODO Auto-generated method stub
		
	}
	
	
	/*
	 * pilot.setRotateSpeed(SEARCH_SPEED);
		pilot.rotate(90 * direction, true);
		this.checkSensor();
		
		direction *= -1;
		pilot.setRotateSpeed(FAST_SPEED);
		pilot.rotate(90 * direction);
		
		pilot.setRotateSpeed(SEARCH_SPEED);
		pilot.rotate(90 * direction, true);
		this.checkSensor();
		
		direction *= -1;
		pilot.setRotateSpeed(FAST_SPEED);
		pilot.rotate(90 * direction);
		
		
		StateGap.lastTurn = direction;
		stateMachine.changeState(LineFollowing.GAP);
	 * 
	 * */

}
