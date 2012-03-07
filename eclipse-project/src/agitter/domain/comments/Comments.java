
package agitter.domain.comments;


import agitter.domain.users.User;

public interface Comments {

	Comment[] commentsFor(Object thing);

	void commentOn(Object thing, User author, String text);

}
