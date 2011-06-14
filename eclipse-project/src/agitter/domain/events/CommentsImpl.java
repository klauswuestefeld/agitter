
package agitter.domain.events;

import java.util.ArrayList;
import java.util.List;

public class CommentsImpl implements Comments {

	@Override
	public List<Comment> commentsFor(Event partyEvent) {
		return new ArrayList<Comment>();
	}

}
