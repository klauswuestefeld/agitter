
package agitter.domain.comments;

import static infra.util.Collections.append;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sneer.foundation.lang.Clock;
import agitter.domain.users.User;

public class CommentsImpl implements Comments {
	
	private static final Comment[] NO_COMMENTS = new Comment[0];
	
	private final Map<Object, Comment[]> commentsByObject = new HashMap<Object, Comment[]>();

	
	@Override
	public Comment[] commentsFor(Object thing) {
		Comment[] ret = originalComments(thing);
		return Arrays.copyOf(ret, ret.length);
	}

	
	@Override
	public void commentOn(Object thing, User author, String text) {
		Comment comment = new CommentImpl(author, Clock.currentTimeMillis(), text);
		Comment[] newComments = append(originalComments(thing), comment);
		commentsByObject().put(thing, newComments);
	}
	
	
	private Comment[] originalComments(Object thing) {
		Comment[] ret = commentsByObject().get(thing);
		return ret == null ? NO_COMMENTS : ret;
	}
	
	private Map<Object, Comment[]>  commentsByObject() {
		return commentsByObject;
	}

}
