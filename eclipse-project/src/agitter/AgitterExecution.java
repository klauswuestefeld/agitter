package agitter;

import agitter.util.Clock;

public class AgitterExecution {

	private EventsImpl _events;

	void initializeHomes(Clock clock) {
		this._events = new EventsImpl(clock);
	}

	public Events events() {
		return _events;
	}

}
