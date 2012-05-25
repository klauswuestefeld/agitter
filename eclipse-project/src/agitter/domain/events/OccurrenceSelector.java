package agitter.domain.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import agitter.domain.users.User;

abstract class OccurrenceSelector {
	
	private final User user;
	List<EventOcurrence> result = new ArrayList<EventOcurrence>();
	
	OccurrenceSelector(User user, SortedSet<EventImpl2> events) {
		this.user = user;
		for(Event e : events)
			visitEvent(e);
		Collections.sort(result);
	}

	private void visitEvent(Event e) {
		if (!e.isVisibleTo(user)) return;
		long[] datetimes = datetimesToVisit(e, user);
		if (datetimes == null) return;
		
		for(long datetime : datetimes)
			result.add(new EventOcurrenceImpl(e, datetime));
	}
	
	abstract long[] datetimesToVisit(Event e, User user);
}