
package agitter.domain.events;

import java.util.List;

import agitter.domain.users.User;

public interface Comments {

	List<Comment> commentsFor(Event event);

	Comment commentOn(Event partyEvent, User owner, String comment);

}
