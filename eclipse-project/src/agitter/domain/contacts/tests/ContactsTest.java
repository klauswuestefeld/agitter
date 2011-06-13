package agitter.domain.contacts.tests;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsImpl;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UsersImpl;


public class ContactsTest extends CleanTestBase {

	private final Contacts subject = new ContactsImpl();

	private User jose;

	
	@Before
	public void initUser() throws Refusal {
		jose = createUser("jose");
	}

	private User createUser(String username) throws Refusal {
		return new UsersImpl().signup( username, username + "@gmail.com", "secret123" );
	}

	@Test
	public void contactsInAlfabeticalOrder() throws Refusal {
		Group all = subject.allContactsFor( jose );
		assertEquals("Todos", all.name());
		assertTrue( all.contacts().isEmpty() );
		
		all.addContact( new EmailAddress( "maria@gmail.com" ) );
		all.addContact( new EmailAddress( "joao@gmail.com" ) );
		all.addContact( new EmailAddress( "joao@gmail.com" ) );
		all.addContact( new EmailAddress( "joAO@gmaiL.com" ) );
		all.addContact( new EmailAddress( "amanda@hotmail.com" ) );
		assertContents( all.contacts(),
			new EmailAddress( "amanda@hotmail.com" ),
			new EmailAddress( "joao@gmail.com" ),
			new EmailAddress( "maria@gmail.com" )
		);
	}

	@Test
	public void multipleUsers() throws Refusal {
		User pedro = createUser("pedro");
		assertNotSame(subject.allContactsFor(pedro), subject.allContactsFor(jose));
	}
	
	
	@Test
	public void subgroupsInAlfabeticalOrder() throws Refusal {
		Group all = subject.allContactsFor( jose );
		
		all.addSubgroup("Friends");
		all.addSubgroup("Work");
		all.addSubgroup("Family");
		List<Group> subgroups = all.subgroups();
		assertEquals( "[Family, Friends, Work]", subgroups.toString() );
	}
	
	@Test
	public void duplicatedSubgroup() throws Refusal {
		subject.allContactsFor(jose).addSubgroup("Friends");
		
		try {
			subject.allContactsFor(jose).addSubgroup("FRIENDS");
			fail();
		}catch(Refusal expected) {
		}
		
		assertEquals(1, subject.allContactsFor(jose).subgroups().size());
	}

	@Test
	public void subgroupWithEmptyName() throws Refusal {
		testInvalidSubgroupName("");
		testInvalidSubgroupName(" ");
		testInvalidSubgroupName("\t");
		testInvalidSubgroupName(null);
	}

	private void testInvalidSubgroupName(String name) {
		try {
			subject.allContactsFor(jose).addSubgroup(name);
			fail();
		}catch(Refusal expected) {
		}
	}
	

}
