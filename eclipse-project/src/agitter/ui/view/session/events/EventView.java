package agitter.ui.view.session.events;

import java.util.Date;
import java.util.List;


public interface EventView {

	interface Boss {
		void onDescriptionEdit(String newText);
		boolean approveInviteesAdd(String invitee);
		void onInviteeRemoved(String invitee);
		void onDateRemoved(Long date);
		void onDateAdded(Long date);
	}

	
	void startReportingTo(Boss boss);
	
	void clear();

	void displayEditting(String description, long[] datetimes, List<String> invitees, int totalInviteesCount);
	void refreshInviteesToChoose(List<String> inviteesToChoose);
	void refreshInvitationsHeader(int totalInviteesCount);

	void displayReadOnly(String owner, String description, Date datetime, List<String> knownInvitees, int totalInviteesCount);

}
