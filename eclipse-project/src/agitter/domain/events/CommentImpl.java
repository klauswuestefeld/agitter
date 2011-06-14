
package agitter.domain.events;

import agitter.domain.users.User;

public class CommentImpl implements Comment {
	
	private final User owner;
	private final String comment;

	public CommentImpl(User owner, String comment) {
		this.owner = owner;
		this.comment = comment;
	}
	
	@Override
	public User owner() {
		return owner;
	}
	
	@Override
	public String comment() {
		return comment;
	}

}
