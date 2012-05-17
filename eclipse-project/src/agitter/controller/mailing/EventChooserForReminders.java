package agitter.controller.mailing;

import java.util.ArrayList;
import java.util.List;

import agitter.domain.events.EventOcurrence;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import utils.DateUtils;

import static java.util.Calendar.FRIDAY;

public class EventChooserForReminders {

	private static final int MAX_EVENTS_TO_SEND = 5;

	private final long _startTime = DateUtils.daysFromNow(1);
	private final long _endTime = endTime();

	private final Events events;


	EventChooserForReminders(Events events) {
		this.events = events;
	}


	List<EventOcurrence> choose(User user) {
		List<EventOcurrence> result = new ArrayList<EventOcurrence>(MAX_EVENTS_TO_SEND);
		List<EventOcurrence> candidates = events.toHappen(user);
		for(EventOcurrence e : candidates) {
			if(result.size()==MAX_EVENTS_TO_SEND) { break; }
			if(!isTimeToRemindAbout(e)) { continue; }
			result.add(e);
		}
		return result;
	}

	private boolean isTimeToRemindAbout(EventOcurrence e) {
		return e.datetime() >= this._startTime && e.datetime() < this._endTime;
	}

	private long endTime() {
		return DateUtils.isToday(FRIDAY)
				? DateUtils.daysFromNow(3)
				: DateUtils.daysFromNow(2);
	}

}
