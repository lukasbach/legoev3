package labyrinth;

import lejos.robotics.navigation.DifferentialPilot;
import main.CourseSectionStateMachine;
import main.State;
import robotcontrol.Robot;

public class Labyrinth extends CourseSectionStateMachine {
	
	public static final int FIND_BEGINNING = 0;
	public static final int FOLLOW_LINE = 1;
	public static final int MAKE_DECISION = 2;
	public static final int FIND_LINE = 3;
	
	@SuppressWarnings("deprecation")
	public Labyrinth(Robot robot, DifferentialPilot pilot) {
		super();
		State[] states = new State[4];
		states[0] = new FindBeginning(this, pilot, robot);
		states[1] = new FollowLine(this, pilot, robot);
		states[2] = new MakeDecision(this, pilot, robot);
		states[3] = new FindLine(this, pilot, robot);
		//states[1] =
		//states[2] =
		// ...
		setStates(states);
		run();
	}

}
