package agitter.domain.contacts;

import org.prevayler.bubble.PrevalentBubble;

public class GroupImpl implements Group {
	
	private String name;

	public GroupImpl(String groupName) {
		name = groupName;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public int compareTo(Group other) {
		return name().compareToIgnoreCase( other.name() );
	}

	@Override
	public String toString() {
		return name();
	}
	
}
