package agitter.server.domain;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import agitter.shared.BusinessException;

public class Events implements Serializable {
	private static final long serialVersionUID = 1L;

	public HashMap<Integer, Event> _events = new HashMap<Integer, Event>();
	private int nextId = 0;

	public Event createFor(User moderator, String description, Date date) throws BusinessException {
		Event event = Event.createFor(nextId(), moderator, description, date);
		
		moderator.addEvent(event);
		
		_events.put(event.getId(), event);
		
		return event;
	}

	public void removeFor(User moderator, int id) throws BusinessException {
		if (!_events.containsKey(id))
			throw new BusinessException("Este agito não exite!");
		
		Event event = _events.get(id);
		
		if (!event.getModerator().equals(moderator))
			throw new BusinessException("Este agito não é seu!");
		
		event.getModerator().removeEvent(event);
		
		_events.remove(id);
	}
	
	private int nextId() {
		return ++nextId;
	}

	public int count() {
		return _events.size();
	}

}