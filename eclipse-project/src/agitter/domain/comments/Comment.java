
package agitter.domain.comments;

import agitter.domain.users.User;

public interface Comment {

	User owner();
	
	long creationDatetime();

	String text();

}
