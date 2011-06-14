
package agitter.domain.events;

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

	private final CacheMap<Event, List<Comment>> commentsByEvent = CacheMap.newInstance();

	@Override
	public List<Comment> commentsFor(final Event partyEvent) {
		return new ArrayList<Comment>(eventComments(partyEvent));
	}

	@Override
	public Comment commentOn(final Event partyEvent, final User owner, final String comment) {
		final Comment ret = new CommentImpl(owner, Clock.currentTimeMillis(), comment);
		eventComments(partyEvent).add(ret);
		
		return ret;
	}
	
	private List<Comment> eventComments(final Event partyEvent) {
		return commentsByEvent.get(partyEvent, NEW_COMMENT_LIST);
	}

}
