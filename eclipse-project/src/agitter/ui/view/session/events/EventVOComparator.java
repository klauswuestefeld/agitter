package agitter.ui.view.session.events;

import java.io.Serializable;
import java.util.Comparator;



public class EventVOComparator implements Comparator<EventVO>, Serializable {

	private static final long serialVersionUID = -3286972461451511508L;

	@Override
	public int compare(EventVO a1, EventVO a2) {
		long result = a1.datetime - a2.datetime; //Casting to int does not have enough precision.
		if (result == 0L)
			result = a1.description.compareTo(a2.description);
		if (result == 0L)
			return 0;
		return result < 0L ? -1 : 1;
	}

}
