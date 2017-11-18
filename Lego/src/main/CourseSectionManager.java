package main;

import lineFollowing.LineFollowing;
import robotcontrol.Drive;
import robotcontrol.Robot;
import robotcontrol.RobotConfig;

public class CourseSectionManager {
	
	private CourseSections currentSection;
	private Robot robot;
	
	public CourseSectionManager(CourseSections startingSection) {
		this.currentSection = startingSection;
		this.robot = new Robot();
		
		//for now
		new LineFollowing(robot);
	}
	
	//do stuff depending on the state. Receive events (barcodes) to switch state.
	
}
