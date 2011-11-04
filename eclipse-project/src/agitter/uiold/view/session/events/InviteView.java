package agitter.uiold.view.session.events;

import java.util.Date;
import java.util.List;


public interface InviteView {

	void reset(List<String> inviteesToChoose);
	void display(String description, Date datetime, List<String> invitees);

	String eventDescription();
	Date datetime();
	List<String> invitees();
	
}
