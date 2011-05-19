package agitter.ui.view;


public class EventData {
	
	public final String description;
	public final long datetime;
	public final String owner;
	public final Runnable onRemoveAction;

	public EventData(String description, long datetime, String owner, Runnable onRemoveAction) {
		this.description = description;
		this.datetime = datetime;
		this.owner = owner;
		this.onRemoveAction = onRemoveAction;
	}
	
}
