package main;

import java.io.File;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.utility.Delay;
import lejos.utility.TextMenu;

public class Menu {
	public static final String MENU_TITLE = "Pls select Start"; //Max 16 chars.
	private TextMenu courseSectionMenu;
	static public CourseSectionManager csmanager;
	static public boolean allowAbort = false;
	static private Menu menuInstance;
	
	public Menu() {
		CourseSections startingSection = displayMenu();
		Sound.twoBeeps();
		csmanager = new CourseSectionManager(startingSection);
		allowAbort = true;
		csmanager.start();
		Sound.beepSequenceUp();
		csmanager = null;
		allowAbort = false;
		//Delay.msDelay(15000);
	}

	/**
	 * Displays all course sections on the EV3 Screen
	 *
	 * @return The starting section selected by the user
	 */
	public CourseSections displayMenu() {
		allowAbort = false;
		Sound.beepSequenceUp();
		//csmanager = null;
		String[] sectionNames = CourseSections.names();
		courseSectionMenu = new TextMenu(sectionNames, 1, MENU_TITLE);
		int selection = courseSectionMenu.select();

		//DEBUG
		for (int i = 0; i < selection; i++) {
			Sound.beep();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (selection < 0 || selection >= sectionNames.length) {
			Sound.beepSequence();
			throw new InternalError("Menü is immernoch am Arsch");
		}

		LocalEV3.get().getGraphicsLCD().clear();
		//Vll. bissel unsafe
		return CourseSections.values()[selection];
	}

	public static void main(String[] args) {
		
		Sound.playSample(new File("./VADRBRTH.wav"), 100);
		Sound.beepSequenceUp();
		
		Button.ESCAPE.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {
			}

			@Override
			public void keyPressed(Key k) {
				Sound.beepSequence();
				System.exit(0);
			}
		});
	Button.ENTER.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {
				/*Sound.beepSequence();
				if (hasInit) {
					csmanager.stop();
					hasInit = true;
					new Menu();
				}*/
				Sound.beep();
				
				if (!allowAbort) return;
				
				csmanager.stop();
				Sound.beep();
				CourseSections startingSection = menuInstance.displayMenu();
				Sound.beep();
				csmanager = new CourseSectionManager(startingSection);
				Sound.beep();
				allowAbort = true;
				Sound.beep();
				csmanager.start();
				Sound.beep();
				Sound.beepSequenceUp();
				Sound.beep();
				csmanager = null;
				allowAbort = false;
			}
			
			@Override
			public void keyPressed(Key k) {
			}
		});
		menuInstance = new Menu();
	}
}
