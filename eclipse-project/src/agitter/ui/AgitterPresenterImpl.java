package agitter.ui;

import agitter.domain.Agitter;
import agitter.domain.Event;

public class AgitterPresenterImpl implements AgitterPresenter {

	private View view;
	private final Agitter agitter;

	public AgitterPresenterImpl(Agitter agitter) {
		this.agitter = agitter;
	}

	@Override
	public void setView(View view) {
		this.view = view;
		
		for (Event event : agitter.events().toHappen())
			view.addEvent(event);
	}

	@Override
	public void add(String description, long time) {
		agitter.domain.Event event = agitter.events().create(description, time);
		view.addEvent(event);
	}

	@Override
	public void remove(Event event) {
		agitter.events().remove(event);
		view.removeEvent(event);
	}
}
