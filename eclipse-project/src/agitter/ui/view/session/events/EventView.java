package agitter.ui.view.session.events;

import java.util.List;


public interface EventView {
	
	public static final boolean COMMENTS_ENABLED = false;

	interface Boss {
		void onDescriptionEdit(String newText);
		boolean approveInviteesAdd(String invitee);
		void onInviteeRemoved(String invitee);
		void onDateRemoved(Long date);
		void onDateAdded(Long date);
		void onCommentPosted(String comment);
	}

	
	void startReportingTo(Boss boss);
	
	void clear();

	void displayEditting(String description, long[] datetimes, List<String> invitees, int totalInviteesCount, List<String> comments);
	void refreshInviteesToChoose(List<String> inviteesToChoose);
	void refreshInvitationsHeader(int totalInviteesCount);

	void displayReadOnly(String owner, String description, long[] datetimes, List<String> knownInvitees, int totalInviteesCount);

}
