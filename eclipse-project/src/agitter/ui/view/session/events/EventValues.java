package agitter.ui.view.session.events;


public class EventValues {

	public final Object eventObject;
	public final String description;
	public final long datetime;
	public final String owner;
	public final boolean isDeletable;

	public EventValues(Object eventObject, String description, long datetime, String owner, boolean isDeletable) {
		this.eventObject = eventObject;
		this.description = description;
		this.datetime = datetime;
		this.owner = owner;
		this.isDeletable = isDeletable;
	}
	
}
