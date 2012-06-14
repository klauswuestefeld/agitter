package agitter.domain.users;

import basis.lang.Functor;
import agitter.domain.emails.EmailAddress;

public class UserUtils {

	public static User produce(Users users, EmailAddress email) {
		User user = users.searchByEmail(email);
		return user == null
			? users.produce(email)
			: user;
	}

	
	public static Functor<EmailAddress, User> userProducer(final Users users) { //Optimize return always the same functor for the same Users.
		return new Functor<EmailAddress, User>() {  @Override public User evaluate(EmailAddress email) {
			return produce(users, email);
		}};
	}

}
