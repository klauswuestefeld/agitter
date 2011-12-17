package agitter.ui.view.session.events;

import java.util.List;


public class EventVO {

	public final Object eventObject;
	public final String description;
	public final long datetime;
	public final String owner;
	public final boolean isDeletable;
	public List<String> invitees;
	public int unknownInvitees;

	public EventVO(Object eventObject, String description, long datetime, String owner, boolean isDeletable, int unknownInvitees, List<String> invitees) {
		this.eventObject = eventObject;
		this.description = description;
		this.datetime = datetime;
		this.owner = owner;
		this.isDeletable = isDeletable;
		this.unknownInvitees = unknownInvitees;
		this.invitees = invitees;
	}
	
}
