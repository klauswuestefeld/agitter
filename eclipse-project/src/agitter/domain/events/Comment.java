
package agitter.domain.events;

import agitter.domain.users.User;

public interface Comment {

	User owner();

	String comment();

}
