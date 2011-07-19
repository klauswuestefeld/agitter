package agitter.ui.view.session.events;

import java.util.Date;
import java.util.List;


public interface InviteView {

	void reset(List<String> inviteesToChoose);

	String eventDescription();
	Date datetime();
	List<String> invitees();
	
}
