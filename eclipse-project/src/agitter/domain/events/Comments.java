
package agitter.domain.events;

import java.util.List;

public interface Comments {

	List<Comment> commentsFor(Event partyEvent);

}
