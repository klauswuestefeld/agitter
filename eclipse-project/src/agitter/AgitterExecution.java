package agitter;

import agitter.util.AgitterClock;

public class AgitterExecution {

	private EventsImpl _events;

	void initializeHomes(AgitterClock clock) {
		this._events = new EventsImpl(clock);
	}

	public Events events() {
		return _events;
	}

}
