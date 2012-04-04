package agitter.controller.oauth.contactsimport;

import infra.logging.LogInfra;

import java.util.List;

import org.brickred.socialauth.Contact;

import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.common.Portal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.AddressValidator;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;


public class ContactsImport extends Thread {

	private final ContactsOfAUser container;
	private final Iterable<Contact> candidatesToImport;
	private final Functor<EmailAddress, User> userProducer;
	private final List<User> existing;
	private final Portal portal;


	public ContactsImport(Portal portal, ContactsOfAUser container, Iterable<Contact> candidatesToImport, Functor<EmailAddress, User> userProducer) {
		this.container = container;
		this.candidatesToImport = candidatesToImport;
		this.userProducer = userProducer;
		this.existing = container.all();
		this.portal = portal;
	}

	@Override
	public void run() {
		produceGroup(portal.name());
		
		for (Contact candidate : candidatesToImport)
			importIfNecessary(candidate);
	}

	
	private void produceGroup(String groupName) {
		if (container.groupGivenName(groupName) != null) return;
		try {
			container.createGroup(groupName);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}

	
	private void importIfNecessary(Contact candidate) {
		User user = asUser(candidate);
		if (user == null) return;
		
		if (!existing.contains(user)) 
			container.addContact(user);
		
		container.addContactTo(container.groupGivenName(portal.name()), user);
	}

	
	private User asUser(Contact candidate) {
		String emailStr = candidate.getEmail(); 
		if (emailStr == null) {
			if (AddressValidator.isValidEmail(candidate.getDisplayName())) {
				emailStr = candidate.getDisplayName();	
			}
		}
			
		if (emailStr == null) return null;
		
		try {
			EmailAddress email = EmailAddress.email(emailStr);
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
		if (candidate.getDisplayName() != null) 
			return candidate.getDisplayName();
		
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
