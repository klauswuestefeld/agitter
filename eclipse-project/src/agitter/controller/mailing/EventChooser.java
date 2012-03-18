package agitter.controller.mailing;

import static java.util.Calendar.FRIDAY;

import java.util.ArrayList;
import java.util.List;

import utils.DateUtils;
import agitter.domain.events.Event;

public class EventChooser {

	private static final int MAX_EVENTS_TO_SEND = 5;

	private final long _startTime = DateUtils.daysFromNow(1);
	private final long _endTime = endTime();


	List<Event> choose(List<Event> candidates) {
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SEND);
		for(Event e : candidates) {
			if (result.size() == MAX_EVENTS_TO_SEND) break;
			if (!isTimeToRemindAbout(e)) continue;
			result.add(e);
		}
		return result;
	}

	
	private boolean isTimeToRemindAbout(Event e) {
		for (long datetime : e.datetimes()) 
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
