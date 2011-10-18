package agitter.ui.view.session.events;


public class EventData {

	public final long id;
	public final String description;
	public final long datetime;
	public final String owner;
	public final Runnable onRemoveAction;
	public final boolean isDeletable;

	public EventData(long id, String description, long datetime, String owner, boolean isDeletable, Runnable onRemoveAction) {
		this.id = id;
		this.description = description;
		this.datetime = datetime;
		this.owner = owner;
		this.isDeletable = isDeletable;
		this.onRemoveAction = onRemoveAction;
	}
	
}
