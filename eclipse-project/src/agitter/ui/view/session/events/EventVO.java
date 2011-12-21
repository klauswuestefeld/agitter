package agitter.ui.view.session.events;

public class EventVO {

	public final Object eventObject;
	public final String description;
	public final long datetime;
	public final String owner;
	public final boolean isEditable;
	public int totalInviteesCount;
	public String uniqueGroupOrUserInvited;
	public final boolean isUniqueUserInvited;


	public EventVO(Object eventObject, String description, long datetime, String owner, boolean isEditable, int totalInviteesCount, String uniqueGroupOrUserInvited, boolean isUniqueUserInvited) {
		this.eventObject = eventObject;
		this.description = description;
		this.datetime = datetime;
		this.owner = owner;
		this.isEditable = isEditable;
		this.totalInviteesCount = totalInviteesCount;
		this.uniqueGroupOrUserInvited = uniqueGroupOrUserInvited;
		this.isUniqueUserInvited = isUniqueUserInvited;
	}
	
}
