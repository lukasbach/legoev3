package main;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.lcd.LCD;
import lejos.utility.TextMenu;

public class Menu {
	public static final String MENU_TITLE = "Pls select Start"; //Max 16 chars.
	private TextMenu courseSectionMenu;
	
	private CourseSectionManager courseSectionManager;
	
	public Menu() {
		CourseSections startingSection = displayMenu();
		courseSectionManager = new CourseSectionManager(startingSection);
	}
	
	/**
	 * Displays all course sections on the EV3 Screen
	 * @return The starting section selected by the user
	 */
	private CourseSections displayMenu() {
		String [] sectionNames = CourseSections.names();
		courseSectionMenu = new TextMenu(sectionNames, 1, MENU_TITLE);
		int selection = courseSectionMenu.select();
		//Vll. bissel unsafe
		return CourseSections.values()[selection];
	}
	
	public static void main(String[] args) {
		System.out.println("Programm loaded. Press any button ...");
		Button.waitForAnyEvent();
		
		Button.ENTER.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {}
			
			@Override
			public void keyPressed(Key k) {
				System.exit(0);
			}
		});
		new Menu();
	}
}
