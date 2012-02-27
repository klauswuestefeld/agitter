
package agitter.domain.comments;

import org.prevayler.bubble.PrevalentBubble;

import agitter.domain.users.User;

public class CommentImpl implements Comment {
	
	private final User owner;
	private final long creationDatetime;
	private final String text;
	
	{
		PrevalentBubble.idMap().register(this);
	}

	public CommentImpl(User owner, long creationDatetime, String text) {
		this.owner = owner;
		this.creationDatetime = creationDatetime;
		this.text = text;
	}
	
	@Override
	public User owner() {
		return owner;
	}
	
	@Override
	public long creationDatetime() {
		return creationDatetime;
	}
	
	@Override
	public String text() {
		return text;
	}
	
	
	@Override
	public String toString() {
		return text();
	}

}
