package agitter.ui.tests;

import java.util.ArrayList;

import agitter.domain.Event;
import agitter.ui.AgitterPresenter;

public class PresenterViewStub implements AgitterPresenter.View {

	ArrayList<Event> events = new ArrayList<Event>(); 
	@Override
	public void addEvent(Event event) {
		events.add(event);
	}

	@Override
	public void removeEvent(Event event) {
		events.remove(event);
	}

	public ArrayList<Event> events() {
		return events;
	}

}
