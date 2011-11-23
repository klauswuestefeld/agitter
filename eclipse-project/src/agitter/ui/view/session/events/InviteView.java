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
	void display(String description, Date datetime, List<String> invitees);
	void enableEdit(boolean canEdit);
	void enableNewEvent();
	
	void focusOnDescription();
	void focusOnDate();

	void refreshInviteesToChoose(List<String> inviteesToChoose);

}
