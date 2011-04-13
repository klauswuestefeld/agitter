package agitter.server.domain.comparators;


import java.util.Comparator;

import agitter.server.domain.User;

public class UserEmailComparator implements Comparator<User> {

	@Override
	public int compare(User user1, User user2) {
		if(user1.getEmail()==null || user2.getEmail()==null)
			return 0;
		return user1.getEmail().compareTo(user2.getEmail());
	}

}
