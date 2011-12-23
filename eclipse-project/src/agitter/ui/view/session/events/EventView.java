package agitter.ui.view.session.events;

import java.util.Date;
import java.util.List;


public interface EventView {

	interface Boss {
		void onDescriptionEdit(String newText);
		void onDatetimeEdit(Date date);
		boolean approveInviteeAdd(String invitee);
		void onInviteeRemoved(String invitee);
	}

	
	void startReportingTo(Boss boss);
	
	void clear();

	void displayEditting(String description, Date datetime, List<String> invitees, int totalInviteesCount);
	void refreshInviteesToChoose(List<String> inviteesToChoose);
	void refreshInvitationsHeader(int totalInviteesCount);

	void displayReadOnly(String owner, String description, Date datetime, List<String> knownInvitees, int totalInviteesCount);

}
