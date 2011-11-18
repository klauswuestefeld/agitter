package agitter.ui.view.session.events;

import java.util.Date;
import java.util.List;


public interface InviteView {

	void clear();
	void refreshInviteesToChoose(List<String> inviteesToChoose);
	void display(String description, Date datetime, List<String> invitees);

	void enableEdit(boolean canEdit);

	String eventDescription();
	Date datetime();
	List<String> invitees();
	
}
