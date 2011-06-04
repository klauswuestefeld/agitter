package agitter.domain.contacts.test;

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

	private User user ;

	
	@Before
	public void initUser() throws Refusal {
		user = new UsersImpl().signup( "jose", "jose@gmail.com", "secret" );
	}

	@Test
	public void contactsInAlfabeticalOrder() throws Refusal {
		Group all = subject.allContactsFor( user );
		assertTrue( all.contacts().isEmpty() );
		
		all.addContact( new EmailAddress( "maria@gmail.com" ) );
		all.addContact( new EmailAddress( "joao@gmail.com" ) );
		all.addContact( new EmailAddress( "joao@gmail.com" ) );
		all.addContact( new EmailAddress( "joAO@gmaiL.com" ) );
		all.addContact( new EmailAddress( "amanda@hotmail.com" ) );
		assertContents( all.contacts(), new EmailAddress( "amanda@hotmail.com" ), new EmailAddress( "joao@gmail.com" ), new EmailAddress( "maria@gmail.com" ) );
	}
	
	@Test
	public void subgroups() throws Refusal {
		Group all = subject.allContactsFor( user );
		
		all.addSubgroup("Friends");
		all.addSubgroup("Work");
		all.addSubgroup("Family");
		List<Group> subgroups = all.subgroups();
		assertEquals( "[Family, Friends, Work]", subgroups.toString() );
	}
	
	@Test
	public void duplicatedSubgroup() throws Refusal {
		subject.allContactsFor(user).addSubgroup("Friends");
		
		try {
			subject.allContactsFor(user).addSubgroup("FRIENDS");
			fail();
		}catch(Refusal expected) {
		}
		
		assertEquals(1, subject.allContactsFor(user).subgroups().size());
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
			subject.allContactsFor(user).addSubgroup(name);
			fail();
		}catch(Refusal expected) {
		}
	}
	

}
