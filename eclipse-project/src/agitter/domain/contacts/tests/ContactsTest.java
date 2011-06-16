package agitter.domain.contacts.tests;

import java.util.List;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsImpl;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UserImpl;


public class ContactsTest extends CleanTestBase {

	private final Contacts subject = new ContactsImpl();

	private final User jose = createUser("jose");
	private final ContactsOfAUser josesContacts = subject.contactsOf(jose);

	
	private User createUser(String username) {
		return new UserImpl(username, username + "@gmail.com", "secret123");
	}

	
	@Test
	public void contactsInAlfabeticalOrder() throws Refusal {
		ContactsOfAUser josesContacts = subject.contactsOf(jose);
		assertTrue(josesContacts.all().isEmpty());
		
		josesContacts.addContact(new EmailAddress( "maria@gmail.com" ) );
		josesContacts.addContact(new EmailAddress( "joao@gmail.com" ) );
		josesContacts.addContact(new EmailAddress( "joao@gmail.com" ) );
		josesContacts.addContact(new EmailAddress( "joAO@gmaiL.com" ) );
		josesContacts.addContact(new EmailAddress( "amanda@hotmail.com" ) );
		assertContents( josesContacts.all(),
			new EmailAddress( "amanda@hotmail.com" ),
			new EmailAddress( "joao@gmail.com" ),
			new EmailAddress( "maria@gmail.com" )
		);
	}

	
	@Test
	public void multipleUsers() throws Refusal {
		User pedro = createUser("pedro");
		assertNotSame(subject.contactsOf(pedro), subject.contactsOf(jose));
	}
	
	
	@Test
	public void groupsInAlfabeticalOrder() throws Refusal {
		
		createGroup(josesContacts, "Friends");
		createGroup(josesContacts, "Work");
		createGroup(josesContacts, "Family");
		List<Group> groups = josesContacts.groups();
		assertEquals( "[Family, Friends, Work]", groups.toString() );
	}
	
	
	@Test
	public void duplicatedSubgroup() throws Refusal {
		createGroup(josesContacts, "Friends");
		
		try {
			createGroup(josesContacts, "FRIENDS");
			fail();
		}catch(Refusal expected) {
		}
		
		assertEquals(1, josesContacts.groups().size());
	}

	
	@Test
	public void subgroupCycle() throws Refusal {
		Group a = createGroup(josesContacts, "GroupA");
		Group b = createGroup(josesContacts, "GroupB");
		a.addSubgroup(b);
		b.addSubgroup(a);
		
		assertFalse(a.deepContains(new EmailAddress("foo@foo.com")));
		josesContacts.addContactTo(b, new EmailAddress("foo@foo.com"));
		assertTrue(a.deepContains(new EmailAddress("foo@foo.com")));
		
	}

	
	@Test
	public void subgroupWithEmptyName() throws Refusal {
		testInvalidGroupName("");
		testInvalidGroupName(" ");
		testInvalidGroupName("\t");
		testInvalidGroupName(null);
	}

	
	private void testInvalidGroupName(String name) {
		try {
			createGroup(josesContacts, name);
			fail();
		}catch(Refusal expected) {
		}
	}
	
	
	public static Group createGroup(ContactsOfAUser contactsOfAUser, String groupName) throws Refusal {
		contactsOfAUser.createGroup(groupName);
		for (Group g : contactsOfAUser.groups())
			if (g.name().equals(groupName))
				return g;
		throw new IllegalStateException("Group not found: " + groupName);
	}
	

}
