package agitter.ui;

import agitter.Agitter;
import agitter.Event;

public class AgitterPresenterImpl implements AgitterPresenter {

	private View view;
	private final Agitter agitter;

	public AgitterPresenterImpl(Agitter agitter) {
		this.agitter = agitter;
	}

	@Override
	public void setView(View view) {
		this.view = view;
		
		for (Event event : agitter.events().all())
			view.addEvent(event);
	}

	@Override
	public void add(String description, long time) {
		agitter.Event event = agitter.events().create(description, time);
		view.addEvent(event);
	}

	@Override
	public void remove(Event event) {
		agitter.events().remove(event);
		view.removeEvent(event);
	}
}
