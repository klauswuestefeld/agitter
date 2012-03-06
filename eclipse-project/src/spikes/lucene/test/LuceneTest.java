package spikes.lucene.test;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import spikes.lucene.LuceneBase;
import agitter.domain.events.Event;
import agitter.domain.events.tests.EventsTestBase;

public class LuceneTest extends EventsTestBase{

	@Test
	public void testSearch() throws Refusal, CorruptIndexException, LockObtainFailedException, IOException, ParseException {
		Event firstEvent = createEvent(ana, "Festa na Casa do Paulo", 11);
		Event secondEvent = createEvent(ana, "Festa na Casa do Pedro", 12);
		Event thirdEvent = createEvent(ana, "Festa na Casa do Paulo V2", 13);

		assertEquals(3,agitter.events().toHappen(ana).size());  
		
		secondEvent.addDate(14);
		secondEvent.addDate(15);
		secondEvent.addDate(16);
		secondEvent.removeDate(12);
		
		LuceneBase base = new LuceneBase();
		base.create(agitter.events(), ana);
		
		List<Event> events = base.search("Casa do Pedro");
		
		assertEquals("Festa na Casa do Pedro",events.get(0).description());
		assertEquals("Festa na Casa do Paulo",events.get(1).description());
		assertEquals("Festa na Casa do Paulo V2",events.get(2).description());
		
		events = base.search("Casa do Paulo");
		
		assertEquals("Festa na Casa do Paulo",events.get(0).description());
		assertEquals("Festa na Casa do Paulo V2",events.get(1).description());
		assertEquals("Festa na Casa do Pedro",events.get(2).description());
		
	}

}
