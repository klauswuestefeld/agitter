package agitter.common;

public enum Portal {
	
	Twitter,
	Facebook,
	Google,
	Yahoo,
	WindowsLive;
	
	public static Portal search(String name) {
		for (Portal portal : values())
			if (portal.name().equals(name)) return portal;
		return null;
	}
	
}