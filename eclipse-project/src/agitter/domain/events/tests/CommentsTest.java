
package agitter.domain.events.tests;

import org.junit.Test;

import agitter.domain.events.Comments;
import agitter.domain.events.CommentsImpl;
import agitter.domain.events.Event;

import sneer.foundation.lang.exceptions.Refusal;

public class CommentsTest extends EventsTestBase {

	@Test
	public void newEventHasNoComments() throws Refusal {
		final Event partyEvent = _subject.create( _ana, "Party at home", 1000 );
		
		final Comments comments = new CommentsImpl();
		
		assertTrue( comments.commentsFor( partyEvent ).isEmpty() );
	}
	
}
