package agitter;

import agitter.util.AgitterClock;

public class Agitter {

	private EventsImpl _events;
	public Agitter() {
		
	}
	public Agitter(AgitterClock clock){
		initializeHomes(clock);
	}
	public void initializeHomes(AgitterClock clock) {
		this._events = new EventsImpl(clock);
	}

	public Events events() {
		return _events;
	}

}
