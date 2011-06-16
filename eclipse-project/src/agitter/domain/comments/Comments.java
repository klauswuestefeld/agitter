
package agitter.domain.comments;

import java.util.List;

import agitter.domain.users.User;

public interface Comments {

	List<Comment> commentsFor(Object thing);

	void commentOn(Object thing, User author, String text);

}
