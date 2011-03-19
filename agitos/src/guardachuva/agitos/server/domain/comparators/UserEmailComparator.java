package guardachuva.agitos.server.domain.comparators;

import guardachuva.agitos.server.domain.User;

import java.util.Comparator;

public class UserEmailComparator implements Comparator<User> {

	@Override
	public int compare(User user1, User user2) {
		if(user1.getEmail()==null || user2.getEmail()==null)
			return 0;
		return user1.getEmail().compareTo(user2.getEmail());
	}

}
