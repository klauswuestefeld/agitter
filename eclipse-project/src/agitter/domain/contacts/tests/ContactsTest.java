package agitter.domain.contacts.tests;

import static infra.util.ToString.findToString;

import java.util.List;

import org.junit.Test;

import basis.lang.exceptions.Refusal;

import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;
import agitter.domain.users.tests.UsersTestBase;

public class ContactsTest extends UsersTestBase {

	private final User jose = createUser("jose");
	private final Contacts subject = agitter.contacts();
	private final ContactsOfAUser josesContacts = subject.contactsOf(jose);

	
	private User createUser(String username) {
		return user(username, username + "@gmail.com", "secret123");
	}

	
	@Test
	public void contactsInAlfabeticalOrder() throws Refusal {
		ContactsOfAUser josesContacts = subject.contactsOf(jose);
		assertTrue(josesContacts.all().isEmpty());
		
		josesContacts.addContact(user("maria@gmail.com"));
		josesContacts.addContact(user("joao@gmail.com"));
		josesContacts.addContact(user("joao@gmail.com"));
		josesContacts.addContact(user("joAO@gmaiL.com"));
		josesContacts.addContact(user("amanda@hotmail.com"));
		assertContents( josesContacts.all(),
			user("amanda@hotmail.com"),
			user("joao@gmail.com"),
			user("maria@gmail.com")
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
		
		assertFalse(a.deepContains(user("foo@foo.com")));
		josesContacts.addContactTo(b, user("foo@foo.com"));
		assertTrue(a.deepContains(user("foo@foo.com")));
	}

	
	@Test
	public void groupWithInvalidName() throws Refusal {
		testInvalidGroupName("");
		testInvalidGroupName(" ");
		testInvalidGroupName("\t");
		testInvalidGroupName(null);
		testInvalidGroupName("abc@c");
		testInvalidGroupName("abcc<");
		testInvalidGroupName("abcc!");
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
		return findToString(contactsOfAUser.groups(), groupName);
	}
	

}
