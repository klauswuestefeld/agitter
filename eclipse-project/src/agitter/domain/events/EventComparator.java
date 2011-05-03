package agitter.domain.events;

import java.io.Serializable;
import java.util.Comparator;



public class EventComparator implements Comparator<PublicEvent>, Serializable {

	private static final long serialVersionUID = -3286972461451511508L;

	@Override
	public int compare(PublicEvent a1, PublicEvent a2) {
		int result = (int) (a1.datetime()-a2.datetime());
		if(result==0) { result = a1.description().compareTo(a2.description()); }
		return result;
	}

}
