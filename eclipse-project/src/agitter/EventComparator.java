package agitter;

import java.io.Serializable;
import java.util.Comparator;


public class EventComparator implements Comparator<Event>, Serializable {

	private static final long serialVersionUID = -3286972461451511508L;

	@Override
	public int compare(Event a1, Event a2) {
		int result = (int) (a1.datetime()-a2.datetime());
		if(result==0) { result = a1.description().compareTo(a2.description()); }
		return result;
	}

}
