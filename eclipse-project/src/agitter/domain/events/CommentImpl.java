
package agitter.domain.events;

import agitter.domain.users.User;

public class CommentImpl implements Comment {
	
	private final User owner;
	private final long creationDatetime;
	private final String comment;

	public CommentImpl(User owner, long creationDatetime, String comment) {
		this.owner = owner;
		this.creationDatetime = creationDatetime;
		this.comment = comment;
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
	public String comment() {
		return comment;
	}

}
