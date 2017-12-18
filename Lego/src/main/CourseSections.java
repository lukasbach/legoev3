package main;

public enum CourseSections {
	FOLLOW_LINE, LABYRINTH, HOOK, BRIDGE, FIND_COLOR;

	public static String[] names() {
		CourseSections[] sections = values();
		String[] names = new String[sections.length];

		for (int i = 0; i < sections.length; i++) {
			names[i] = sections[i].name();
		}

		return names;
	}
}
