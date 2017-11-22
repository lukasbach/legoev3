package labyrinth;

import lejos.robotics.navigation.DifferentialPilot;
import main.CourseSectionStateMachine;
import main.State;
import robotcontrol.Robot;

public class Labyrinth extends CourseSectionStateMachine {

	@SuppressWarnings("deprecation")
	public Labyrinth(Robot robot, DifferentialPilot pilot) {
		super();
		State[] states = new State[1];
		states[0] = new FollowLine(this, pilot, robot);
		//states[1] =
		//states[2] =
		// ...
		setStates(states);
		run();
	}

}
