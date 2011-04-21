package agitter.ui.view;

public class EventData {
	
	public final String _description;
	public final long _datetime;
	public final String _ownerEmail;
	public final Runnable _onRemoveAction;

	public EventData(String description, long datetime, String ownerEmail, Runnable onRemoveAction) {
		_description = description;
		_datetime = datetime;
		_ownerEmail = ownerEmail;
		_onRemoveAction = onRemoveAction;
	}
	
}
