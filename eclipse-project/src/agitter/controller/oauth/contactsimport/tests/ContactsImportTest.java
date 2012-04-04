package agitter.controller.oauth.contactsimport.tests;

import java.util.Arrays;

import org.brickred.socialauth.Contact;
import org.junit.Test;

import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.common.Portal;
import agitter.controller.oauth.contactsimport.ContactsImport;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;
import agitter.domain.users.tests.UsersTestBase;


public class ContactsImportTest extends UsersTestBase {

	private final Contact c1 = contact("c1@mail.com", "C1", null);
	private final Contact c2 = contact("c2@mail.com", null, "C2");
	private final Contact c3 = contact("c3@mail.com", "C3", "C3");
	private final Contact c4 = contact("c4@mail.com", null, null);
	
	@Test
	public void contactsImport() throws Refusal {
		User ana = user("ana@mail.com");
		ContactsOfAUser contacts = agitter.contacts().contactsOf(ana);

		new ContactsImport(Portal.Google, contacts, Arrays.asList(c1, c2, c3, c4), userProducer())
			.run();
		
		assertEquals("c1@mail.com", contacts.all().get(0).email().toString());
		assertEquals("c2@mail.com", contacts.all().get(1).email().toString());
		assertEquals("c3@mail.com", contacts.all().get(2).email().toString());
		
		assertEquals("C1", contacts.all().get(0).name());
		assertEquals("C2", contacts.all().get(1).name());
		assertEquals("C3 C3", contacts.all().get(2).name());
		assertEquals(null, contacts.all().get(3).name());
	}

	
	private Functor<EmailAddress, User> userProducer() {
		return UserUtils.userProducer(agitter.users());
	}


	private Contact contact(String email, String firstName, String lastName) {
		Contact result = new Contact();
		result.setFirstName(firstName);
		result.setLastName(lastName);
		result.setEmail(email);
		return result;
	}
	
}
