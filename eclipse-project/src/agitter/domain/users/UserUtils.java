package agitter.domain.users;

import agitter.domain.emails.EmailAddress;

public class UserUtils {

	public static User produce(Users users, EmailAddress email) {
		User user = users.searchByEmail(email);
		return user == null
			? users.produce(email)
			: user;
	}

}
