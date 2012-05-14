package agitter.controller.mailing;

import static java.util.Calendar.FRIDAY;

import java.util.ArrayList;
import java.util.List;

import utils.DateUtils;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;

public class EventChooserForReminders {

	private static final int MAX_EVENTS_TO_SEND = 5;

	private final long _startTime = DateUtils.daysFromNow(1);
	private final long _endTime = endTime();

	private final Events events;


	EventChooserForReminders(Events events) {
		this.events = events;
	}


	List<Event> choose(User user) {
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SEND);
		List<Event> candidates = events.toHappen(user);
		for (Event e : candidates) {
			if (result.size() == MAX_EVENTS_TO_SEND) break;
			if (!isTimeToRemindAbout(e, user)) continue;
			result.add(e);
		}
		return result;
	}

	
	private boolean isTimeToRemindAbout(Event e, User user) {
		for (long datetime : e.datetimesInterestingFor(user)) 
			if (datetime >= this._startTime && datetime < this._endTime)
				return true;
		
		return false;
	}
	

	private long endTime() {
		return DateUtils.isToday(FRIDAY)
			? DateUtils.daysFromNow(3)
			: DateUtils.daysFromNow(2);
	}

}
