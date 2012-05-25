package agitter.domain.events;

public interface EventOcurrence extends Comparable<EventOcurrence> {
	Event event();
	long datetime();
}
