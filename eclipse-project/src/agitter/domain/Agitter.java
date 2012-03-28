package agitter.domain;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.comments.Comments;
import agitter.domain.contacts.Contacts;
import agitter.domain.events.Events;
import agitter.domain.mailing.Mailing;
import agitter.domain.users.Users;
import agitter.domain.users.Users.UserNotFound;

public interface Agitter {

	Users users();
	Contacts contacts();
	
	Events events();
	Comments comments();
	
	Mailing mailing();
	
	
	//---------------------------------------
	void migrateSchemaIfNecessary();
	void joinAccounts(String string, String email) throws UserNotFound, Refusal;

}
