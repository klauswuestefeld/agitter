
package agitter.domain.comments.tests;

import static infra.util.ToString.findToString;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.comments.Comment;
import agitter.domain.comments.Comments;
import agitter.domain.comments.CommentsImpl;
import agitter.domain.events.Event;
import agitter.domain.events.tests.EventsTestBase;
import agitter.domain.users.User;

public class CommentsTest extends EventsTestBase {

	private final Comments comments = new CommentsImpl();

	
	@Test
	public void newEventHasNoComments() throws Refusal {
		final Event partyEvent = _subject.create(_ana, "Party at home", 1000);
		assertTrue(comments.commentsFor(partyEvent).isEmpty());
	}
	
	
	@Test
	public void chat() throws Refusal {
		final Event partyEvent = _subject.create(_ana, "Barbecue at home", 1000);
		Clock.setForCurrentThread( 10 );
		
		Comment joseComment = commentOn(partyEvent, _jose, "Cool! Need help?");
		assertEquals(_jose, joseComment.owner());
		assertEquals(10, joseComment.creationDatetime());
		assertEquals("Cool! Need help?", joseComment.text());
		
		Comment anaComment = commentOn(partyEvent, _ana, "No, just bring your favorite beverages!");

		assertContents(comments.commentsFor(partyEvent), joseComment, anaComment);
	}

	
	private Comment commentOn(Event event, User user, String text) {
		comments.commentOn(event, user, text);
		return findToString(comments.commentsFor(event), text);
	}

}
