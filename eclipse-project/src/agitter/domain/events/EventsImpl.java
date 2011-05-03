package agitter.domain.events;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.User;

public class EventsImpl implements Events, Serializable {

	private static final long serialVersionUID = 1L;

	private SortedSet<PublicEvent> _all = new TreeSet<PublicEvent>( new EventComparator() );
	
	
	@Override
	public PublicEvent create(User user, String description, long datetime) throws Refusal {
		assertIsInTheFuture(datetime);
		
		PublicEvent publicEvent = new PublicEvent(description, datetime);
		_all.add(publicEvent);
		return publicEvent;
	}


	private void assertIsInTheFuture(long datetime) throws Refusal {
		if (datetime < Clock.currentTimeMillis())
			throw new Refusal("Novos eventos devem ser criados com data futura.");
	}

	
	@Override
	public SortedSet<PublicEvent> toHappen(User user) {
		SortedSet<PublicEvent> all = _all.tailSet(new PublicEvent("Dummy", Clock.currentTimeMillis()));
		SortedSet<PublicEvent> toHappenForTheUser = new TreeSet<PublicEvent>(all);
		for(PublicEvent e: all) {
			if(e.getNotInterested().contains(user)) {
				toHappenForTheUser.remove(e);
			}
		}
		return toHappenForTheUser;
	}


//	@Override
//	public void remove(User user, PublicEvent publicEvent) {
//		_all.remove(publicEvent);
//	}

}
