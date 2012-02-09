
package agitter.domain.comments;

import static infra.util.Collections.copy;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.CacheMap;
import sneer.foundation.lang.Clock;
import sneer.foundation.lang.Producer;
import agitter.domain.users.User;

public class CommentsImpl implements Comments {
	
	private static final Producer<List<Comment>> NEW_COMMENT_LIST = new Producer<List<Comment>>() { @Override public List<Comment> produce() {
		return new ArrayList<Comment>();
	}};

	private final CacheMap<Object, List<Comment>> commentsByEvent = CacheMap.newInstance();

	@Override
	public List<Comment> commentsFor(Object thing) {
		return copy(mutableCommentsFor(thing));
	}

	@Override
	public void commentOn(Object thing, User author, String text) {
		System.err.println( "commentOn: " + author + " saying: " + text + " at: " + thing );
		Comment comment = new CommentImpl(author, Clock.currentTimeMillis(), text);
		mutableCommentsFor(thing).add(comment);
	}
	
	private List<Comment> mutableCommentsFor(Object thing) {
		return commentsByEvent.get(thing, NEW_COMMENT_LIST);
	}

}
