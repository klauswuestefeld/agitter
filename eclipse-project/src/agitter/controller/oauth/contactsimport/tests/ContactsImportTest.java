package agitter.controller.oauth.contactsimport.tests;

import java.util.Arrays;

import org.brickred.socialauth.Contact;
import org.junit.Test;

import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.controller.oauth.contactsimport.ContactsImport;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;
import agitter.domain.users.tests.UsersTestBase;


public class ContactsImportTest extends UsersTestBase {

	private final Contact c1 = contact("c1@mail.com");
	private final Contact c2 = contact("c2@mail.com");
	private final Contact c3 = contact("c3@mail.com");
	
	@Test
	public void contactsImport() throws Refusal {
		User ana = user("ana@mail.com");
		ContactsOfAUser contacts = agitter.contacts().contactsOf(ana);

		new ContactsImport(contacts, Arrays.asList(c1, c2, c3), userProducer());
		
		assertEquals("c1@mail.com", contacts.all().get(0).email().toString());
		assertEquals("c2@mail.com", contacts.all().get(1).email().toString());
		assertEquals("c3@mail.com", contacts.all().get(2).email().toString());
	}

	
	private Functor<EmailAddress, User> userProducer() {
		return UserUtils.userProducer(agitter.users());
	}


	private Contact contact(String email) {
		Contact result = new Contact();
		result.setEmail(email);
		return result;
	}
	
}
