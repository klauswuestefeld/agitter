package agitter.controller.oauth.contactsimport;

import infra.logging.LogInfra;

import java.util.List;

import org.brickred.socialauth.Contact;

import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;


public class ContactsImport extends Thread {

	private final ContactsOfAUser container;
	private final Iterable<Contact> candidatesToImport;
	private final Functor<EmailAddress, User> userProducer;
	private final List<User> existing;
	private final String group;


	public ContactsImport(String group, ContactsOfAUser container, Iterable<Contact> candidatesToImport, Functor<EmailAddress, User> userProducer) {
		this.container = container;
		this.candidatesToImport = candidatesToImport;
		this.userProducer = userProducer;
		this.existing = container.all();
		this.group = group;
	}

	@Override
	public void run() {
		Group g = container.groupGivenName(group);
		if (g==null) {
			try {
				container.createGroup(group);
			} catch (Refusal e) {
				throw new sneer.foundation.lang.exceptions.NotImplementedYet(e);
			}
		}
		
		for (Contact candidate : candidatesToImport)
			importIfNecessary(candidate);
	}

	
	private void importIfNecessary(Contact candidate) {
		User user = asUser(candidate);
		if (user == null || existing.contains(user)) return;
		container.addContact(user);
		container.addContactTo(container.groupGivenName(group), user);
	}

	
	private User asUser(Contact candidate) {
		if (candidate.getEmail() == null) return null;
		try {
			EmailAddress email = EmailAddress.email(candidate.getEmail());
			User u = userProducer.evaluate(email);
			
			if (u != null) 
				u.setName(getName(candidate));
			
			return u;
		} catch (Refusal e) {
			LogInfra.getLogger(this).warning("Illegal email address being imported: " + candidate.getEmail());
			return null;
		}
	}
	
	public String getName(Contact candidate) {
		String name = "";
		
		if (candidate.getFirstName() != null)
			name = candidate.getFirstName() + " ";
		
		if (candidate.getLastName() != null) 
			name = name + candidate.getLastName();
		
		if (name.trim().equals(""))
			return null;
		
		return name.trim();
	}

}
