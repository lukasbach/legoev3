package main;

import bridgeCrossingPID.BridgeCrossing;
import findColor.FindingColor;
import labyrinth.Labyrinth;
import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lineFollowing.LineFollowing;
import robotcontrol.Robot;

public class CourseSectionManager {

	private CourseSections currentSection;
	private static Robot robot;
	private DifferentialPilot pilot;
	private CourseSectionStateMachine currentMachine;

	public CourseSectionManager(CourseSections startingSection) {
		this.currentSection = startingSection;
		if (CourseSectionManager.robot == null) CourseSectionManager.robot = new Robot();
		this.pilot = new DifferentialPilot(30, 160, robot.motors.leftMotor, robot.motors.rightMotor, true);
		//robot.motors.hookMotor.setSpeed(350);
		//robot.motors.hookMotor.backward();
		
	}
		
	public void start() {
		switch (this.currentSection) {
			case FOLLOW_LINE:
				currentMachine = new LineFollowing(robot, pilot);
				break;

			case BRIDGE:
				currentMachine = new BridgeCrossing(robot, pilot);
				break;

			case LABYRINTH:
				currentMachine = new Labyrinth(robot, pilot);
				break;
				
			case FIND_COLOR:
				currentMachine = new FindingColor(robot, pilot);
				break;
			//case HOOK:
			//	robot.motors.hookMotor.setSpeed(50);
			//	robot.motors.hookMotor.backward();
			//	pilot.setTravelSpeed(10);
			//	pilot.travel(500);
			//	while(true) {
			//		
			//	}	
				
			default:
				Sound.beepSequence();
				throw new InternalError("Menue is immernoch am Arsch");
		}
		
		currentMachine.run();
	}
	
	public void stop() {
		if (currentMachine == null) System.out.println("machine is null");
		else currentMachine.stop();
	}

	//do stuff depending on the state. Receive events (barcodes) to switch state.

}
