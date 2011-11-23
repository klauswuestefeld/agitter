package agitter.ui.view.session.events;

import java.util.Date;
import java.util.List;


public interface InviteView {

	interface Boss {
		void onDescriptionEdit(String newText);
		void onDatetimeEdit(Date date);
		boolean approveInviteeAdd(String invitee);
		void onInviteeRemoved(String invitee);
	}

	
	void startReportingTo(Boss boss);
	
	void clear();
	void refreshInviteesToChoose(List<String> inviteesToChoose);

	void display(String description, Date datetime, List<String> invitees);
	void enableEdit(boolean canEdit);
	void enableNewEvent(boolean canEdit);
	
	void focusOnDescription();
	void focusOnDate();

}
