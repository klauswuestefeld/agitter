package agitter;


public class AgitterImpl implements Agitter {

	private final EventsImpl _events = new EventsImpl();
	
	@Override
	public Events events() {
		return _events;
	}

}
