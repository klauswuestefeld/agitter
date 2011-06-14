
package agitter.domain.events.tests;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.Comment;
import agitter.domain.events.Comments;
import agitter.domain.events.CommentsImpl;
import agitter.domain.events.Event;

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
		
		Comment joseComment = comments.commentOn(partyEvent, _jose, "Cool! Need help?");
		assertEquals(_jose, joseComment.owner());
		assertEquals("Cool! Need help?", joseComment.comment());
		
		Comment anaComment = comments.commentOn(partyEvent, _ana, "No, just bring your favorite beverages!");

		assertContents(comments.commentsFor(partyEvent), new Comment[]{joseComment, anaComment} );
	}
	
}
