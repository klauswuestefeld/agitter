package agitter;


public class Agitter {

	private final EventsImpl _events = new EventsImpl();
	
	public Events events() {
		return _events;
	}

}
