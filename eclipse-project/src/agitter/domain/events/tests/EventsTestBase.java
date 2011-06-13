package agitter.domain.events.tests;

import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;
import agitter.domain.users.User;
import agitter.domain.users.UserImpl;

public abstract class EventsTestBase extends CleanTestBase {

	protected final Events _subject = new EventsImpl();
	protected final User _ana = new UserImpl("Ana", "ana@email.com", "123x");
	protected final User _jose = new UserImpl("Jose", "jose@email.com", "123x");


}