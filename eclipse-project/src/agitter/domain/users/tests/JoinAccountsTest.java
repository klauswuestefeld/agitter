package agitter.domain.users.tests;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.events.tests.EventsTestBase;
import agitter.domain.users.User;
import agitter.domain.users.Users.UserNotFound;

public class JoinAccountsTest extends EventsTestBase {

	@Test
	public void joinAccounts() throws Refusal {
		Event ana1 = createEvent(ana, "Evento da Ana 1", 10, paulo, jose);
		Event ana2 = createEvent(ana, "Evento da Ana 2", 10, paulo);
		Event ana3 = createEvent(ana, "Evento da Ana 3", 10, paulo, jose);
		
		Event jose1 = createEvent(jose, "Evento do Jose 1", 10, paulo, jose);
		Event jose2 = createEvent(jose, "Evento do Jose 2", 10, ana);
		Event jose3 = createEvent(jose, "Evento do Jose 3", 10);
		
		Event paulo1 = createEvent(paulo, "Evento do Paulo 1", 10, jose);
		Event paulo2 = createEvent(paulo, "Evento do Paulo 2", 10, jose);
		Event paulo3 = createEvent(paulo, "Evento do Paulo 3", 10, jose);
		
		assertContentsInAnyOrder(
				toEvents(agitter.events().toHappen(ana)),
				ana1, ana2, ana3, jose2);
		
		assertContentsInAnyOrder(
				toEvents(agitter.events().toHappen(jose)),
				ana1, ana3, jose1, jose2, jose3, paulo1, paulo2, paulo3);	
		
		assertContentsInAnyOrder(
				toEvents(agitter.events().toHappen(paulo)),
				ana1, ana2, ana3, jose1, paulo1, paulo2, paulo3);	
		
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
		agitter.contacts().contactsOf(jose).addContactTo(g, pedro);
		agitter.contacts().contactsOf(jose).addContactTo(g2, joao);
		
		assertEquals(0, agitter.contacts().contactsOf(ana).all().size());
		assertContentsInAnyOrder(agitter.contacts().contactsOf(paulo).all(), jose);
		assertContentsInAnyOrder(agitter.contacts().contactsOf(jose).all(), paulo, pedro, joao);
			
		agitter.mergeAccountsIfNecessary(ana.email().toString(), jose.email().toString());
		
		assertContentsInAnyOrder(
				toEvents(agitter.events().toHappen(ana)),
				ana1, ana2, ana3, jose1, jose2, jose3, paulo1, paulo2, paulo3);	
		
		assertContentsInAnyOrder(
				toEvents(agitter.events().toHappen(jose)),
				ana1, ana3, paulo1, paulo2, paulo3);
		
		try {
			agitter.users().findByEmail(EmailAddress.email("jose@email.com"));
			fail();
		} catch (UserNotFound e) {}
		
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
	protected final User joao = user("João", "joao@email.com", "123x");

}
