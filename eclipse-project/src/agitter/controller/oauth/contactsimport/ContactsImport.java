package agitter.controller.oauth.contactsimport;

import infra.logging.LogInfra;

import java.util.List;

import org.brickred.socialauth.Contact;

import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;


public class ContactsImport extends Thread {

	private final ContactsOfAUser container;
	private final Iterable<Contact> candidatesToImport;
	private final Functor<EmailAddress, User> userProducer;
	private final List<User> existing;


	public ContactsImport(ContactsOfAUser container, Iterable<Contact> candidatesToImport, Functor<EmailAddress, User> userProducer) {
		this.container = container;
		this.candidatesToImport = candidatesToImport;
		this.userProducer = userProducer;
		this.existing = container.all();
	}
	
	@Override
	public void run() {
		for (Contact candidate : candidatesToImport)
			importIfNecessary(candidate);
	}

	
	private void importIfNecessary(Contact candidate) {
		User user = asUser(candidate);
		if (user == null || existing.contains(user)) return;
		container.addContact(user);
	}

	
	private User asUser(Contact candidate) {
		if (candidate.getEmail() == null) return null;
		try {
			EmailAddress email = EmailAddress.email(candidate.getEmail());
			return userProducer.evaluate(email);
		} catch (Refusal e) {
			LogInfra.getLogger(this).warning("Illegal email address being imported: " + candidate.getEmail());
			return null;
		}
	}

}