package agitter.domain.users.tests;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.domain.users.Users.UserNotFound;

public class JoinAccountsTest extends UsersTestBase {

	@Test
	public void joinAccounts() throws Refusal {
		createEvent(ana, "Evento da Ana 1", 10, paulo, jose);
		createEvent(ana, "Evento da Ana 2", 10, paulo);
		createEvent(ana, "Evento da Ana 3", 10, paulo, jose);
		
		createEvent(jose, "Evento do Jose 1", 10, paulo, jose);
		createEvent(jose, "Evento do Jose 2", 10, ana);
		createEvent(jose, "Evento do Jose 3", 10);
		
		createEvent(paulo, "Evento do Paulo 1", 10, jose);
		createEvent(paulo, "Evento do Paulo 2", 10, jose);
		createEvent(paulo, "Evento do Paulo 3", 10, jose);
		
		assertEquals(4, agitter.events().toHappen(ana).size());
		assertEquals(8, agitter.events().toHappen(jose).size());
		assertEquals(7, agitter.events().toHappen(paulo).size());
		
		assertEquals(0, agitter.contacts().contactsOf(ana).all().size());
		assertEquals(0, agitter.contacts().contactsOf(paulo).all().size());
		assertEquals(0, agitter.contacts().contactsOf(jose).all().size());
		
		agitter.contacts().contactsOf(paulo).addContact(jose);
		agitter.contacts().contactsOf(jose).addContact(paulo);
		
		Group g = agitter.contacts().contactsOf(jose).createGroup("GrupoTeste");
		Group g2 = agitter.contacts().contactsOf(jose).createGroup("GrupoTeste2");
		Group g3 = agitter.contacts().contactsOf(jose).createGroup("GrupoTeste3");
		g2.addSubgroup(g3);
		agitter.contacts().contactsOf(jose).addContactTo(g, paulo);
		agitter.contacts().contactsOf(jose).addContactTo(g, user("Pedro", "pedro@demo.com", "1234"));
		agitter.contacts().contactsOf(jose).addContactTo(g2, user("João", "joao@demo.com", "1234"));
		
		assertEquals(0, agitter.contacts().contactsOf(ana).all().size());
		assertEquals(1, agitter.contacts().contactsOf(paulo).all().size());
		assertEquals(3, agitter.contacts().contactsOf(jose).all().size());
		
		agitter.joinAccounts(ana.email().toString(), jose.email().toString());
		
		assertEquals(9, agitter.events().toHappen(ana).size());
		assertEquals(6, agitter.events().toHappen(jose).size());
		
		try {
			agitter.users().findByEmail(EmailAddress.email("jose@email.com"));
			fail();
		} catch (UserNotFound e) {
			assertNotNull(e);
		}
		
		assertEquals(3, agitter.contacts().contactsOf(ana).all().size());
		assertEquals(1, agitter.contacts().contactsOf(paulo).all().size());
		assertEquals(3, agitter.contacts().contactsOf(jose).all().size());
		
		assertNotNull(agitter.contacts().contactsOf(ana).groupGivenName("GrupoTeste"));
		assertEquals(2, agitter.contacts().contactsOf(ana).groupGivenName("GrupoTeste").immediateMembers().size());
		assertNotNull(agitter.contacts().contactsOf(ana).groupGivenName("GrupoTeste2"));
		assertEquals(1, agitter.contacts().contactsOf(ana).groupGivenName("GrupoTeste2").immediateMembers().size());
		assertNotNull(agitter.contacts().contactsOf(ana).groupGivenName("GrupoTeste3"));
		assertTrue(agitter.contacts().contactsOf(ana).groupGivenName("GrupoTeste").deepContains(paulo));
		assertEquals(0,agitter.contacts().contactsOf(ana).groupGivenName("GrupoTeste3").immediateSubgroups().size());
	}

	protected final Events subject = agitter.events();
	protected final User ana = user("Ana", "ana@email.com", "123x");
	protected final User jose = user("Jose", "jose@email.com", "123x");
	protected final User paulo = user("Paulo", "paulo@email.com", "123x");
	
	
	protected Event createEvent(User owner, String description, long startTime, User... invitees) throws Refusal {
		return createEvent(subject, owner, description, startTime, invitees);
	}

	protected Event createEvent(Events events, User owner, String description, long startTime, User... invitees) throws Refusal {
		Event event = events.create(owner, description, startTime);
		for (User user : invitees)
			event.invite(owner, user);
		for (Event candidate : events.toHappen(owner))
			if (candidate.description().equals(description) && candidate.datetimes()[0] == startTime)
				return candidate;
		throw new IllegalStateException("Newly created event not found.");
	}

}
