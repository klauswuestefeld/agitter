package agitter.ui.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import agitter.Agitter;
import agitter.Event;
import agitter.ui.AgitterPresenter;
import agitter.ui.AgitterPresenterImpl;
import agitter.util.AgitterClockMock;


public class AgitterPresenterTest {
	
	private AgitterPresenter presenter;
	private Agitter agitter;
	private final AgitterClockMock clock = new AgitterClockMock();
	private final PresenterViewStub view = new PresenterViewStub();

	@Before
	public void setUp() {
		agitter = new Agitter(clock);
		presenter = new AgitterPresenterImpl(agitter);
		presenter.setView(view);
	}
	
	@Test
	public void shouldAddEvents() {
		addSampleToPresenter();
		assertSizeOfEvents(1);
	}
	
	@Test
	public void shouldRemoveAnEvent(){
		addSampleToPresenter();
		Event e = agitter.events().all().first();
		presenter.remove(e);
		assertSizeOfEvents(0);
	}

	private void assertSizeOfEvents(int numberOfEvents) {
		assertEquals(numberOfEvents, agitter.events().all().size());
		assertEquals(numberOfEvents, view.events().size());
	}
	
	private void addSampleToPresenter(){
		presenter.add("my new event", 1);
	}
}


