package main;

import robotcontrol.PortNotDefinedException;

public class CourseSectionStateMachine {

	private int DELAY = 20; //ms
	
	
	private int state = 0;
	private State[] states;
	
	protected CourseSectionStateMachine() {
	}
	
	protected void run() {
		states[state].init();

		while (true) {
			try {
				states[state].run();
				Thread.sleep(DELAY);
			} catch (PortNotDefinedException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void changeState(int state) {
		this.states[this.state].leave();
		this.state = state;
		this.states[state].init();
	}

	protected void setStates(State[] states) {
		if (states.length < 1) {
			throw new IllegalArgumentException("At least one state is needed for a statemachine to work");
		}
		this.states = states;
	}
	
	
	
	
	
}
