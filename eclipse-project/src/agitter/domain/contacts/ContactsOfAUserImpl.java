package agitter.domain.contacts;

import java.util.ArrayList;
import java.util.List;

import agitter.domain.emails.EmailAddress;

@Deprecated
public class ContactsOfAUserImpl {

	final List<EmailAddress> all = new ArrayList<EmailAddress>();
	final List<Group> groups = new ArrayList<Group>();

}
