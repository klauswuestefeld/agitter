package agitter.ui;

import agitter.domain.Event;

public interface AgitterPresenter extends EventAdder, EventRemover {

	public interface View {
		void addEvent(Event event); 
		void removeEvent(Event event);
	}

	void setView(View view);
}
