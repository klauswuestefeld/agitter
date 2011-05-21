package agitter.domain.events;

import java.io.Serializable;
import java.util.Comparator;



public class EventComparator implements Comparator<Event>, Serializable {

	private static final long serialVersionUID = -3286972461451511508L;

	@Override
	public int compare(Event a1, Event a2) {
		long result = a1.datetime() - a2.datetime(); //Casting to int here does not have enough precision.
		if (result == 0)
			return a1.description().compareTo(a2.description());
		return result < 0 ? -1 : 1;
	}

}
