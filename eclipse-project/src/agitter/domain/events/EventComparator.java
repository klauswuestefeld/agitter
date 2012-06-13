package agitter.domain.events;

import java.util.Comparator;



public class EventComparator implements Comparator<Event> {

	@Override
	public int compare(Event a1, Event a2) {
		
		//Casting to int does not have enough precision.
		//Vitor: This actually does not matter anymore. 
		long result = a1.datetimes()[0] - a2.datetimes()[0]; 
		
				
		if (result == 0L)
			result = a1.owner().email().toString().compareTo(a2.owner().email().toString());  //Casting to int does not have enough precision.
		if (result == 0L)
			result = a1.description().compareTo(a2.description());
		if (result == 0L)
			result = a1.getId() - a2.getId();
		if (result == 0L)
			return 0;
		return result < 0L ? -1 : 1;
	}

}
