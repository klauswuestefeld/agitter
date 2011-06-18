package agitter.ui.view;

import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Predicate;


public interface InviteView {

	void reset(List<String> inviteesToChoose, Predicate<String> newInviteeValidator, Runnable onInvite);

	String eventDescription();
	Date datetime();
	List<String> invitees();
	
}
