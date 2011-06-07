package agitter.domain;

import agitter.domain.contacts.Contacts;
import agitter.domain.events.Events;
import agitter.domain.mailing.Mailing;
import agitter.domain.users.Users;

public interface Agitter {

	Users users();
	
	Events events();

	Contacts contacts();
	
	Mailing mailing();

}
