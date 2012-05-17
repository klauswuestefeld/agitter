/**
 * This code is property of Sumersoft Tecnologia Ltda.
 */
package agitter.domain.events;

public class EventOcurrenceImpl implements EventOcurrence {

	private final Event event;
	private final long datetime;

	public EventOcurrenceImpl(Event event, long datetime) {
		this.event = event;
		this.datetime = datetime;
	}

	@Override
	public Event event() {
		return event;
	}

	@Override
	public long datetime() {
		return datetime;
	}

	@Override
	public int compareTo(EventOcurrence other) {
		if(datetime!=other.datetime()) { return (int) (datetime-other.datetime()); }
		int comparison = event.owner().screenName().compareTo(other.event().owner().screenName());
		if(comparison==0) { comparison = event.description().compareTo(other.event().description()); }
		return comparison;
	}

	@Override
	public boolean equals(Object o) {
		if(this==o) { return true; }
		if(o==null || getClass()!=o.getClass()) { return false; }

		EventOcurrenceImpl that = (EventOcurrenceImpl) o;

		if(datetime!=that.datetime) { return false; }
		if(event!=null ? !event.equals(that.event) : that.event!=null) { return false; }

		return true;
	}

	@Override
	public int hashCode() {
		int result = event!=null ? event.hashCode() : 0;
		result = 31*result+(int) (datetime^(datetime >>> 32));
		return result;
	}

}
