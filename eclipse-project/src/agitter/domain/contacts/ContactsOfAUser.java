package agitter.domain.contacts;

import java.util.List;

import org.prevayler.bubble.Transaction;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public interface ContactsOfAUser {

	List<EmailAddress> all();
	void addContact(EmailAddress contact);
	void deleteContactAndRemoveFromAllGroups(EmailAddress contact);

	List<Group> groups();
	@Transaction
	Group createGroup(String groupName) throws Refusal;
	void renameGroup(Group group, String newName) throws Refusal;
	void deleteGroupAndRemoveFromAllContainingGroups(Group group);
	void addContactTo(Group group, EmailAddress contact);
	void removeContactFrom(Group group, EmailAddress contact);

	Group groupGivenName(String name);
}
