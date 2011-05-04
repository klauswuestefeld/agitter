package agitter.ui.view;


public class EventData {
	
	public final String _description;
	public final long _datetime;
	public final String _owner;
	public final Runnable _onRemoveAction;

	public EventData(String description, long datetime, String owner, Runnable onRemoveAction) {
		_description = description;
		_datetime = datetime;
		_owner = owner;
		_onRemoveAction = onRemoveAction;
	}
	
}
