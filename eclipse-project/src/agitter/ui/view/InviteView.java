package agitter.ui.view;

import java.util.Date;
import java.util.List;


public interface InviteView {

	void setContacts(List<String> contacts);

	void onInvite(Runnable action);
	
	String getEventDescription();
	Date getDatetime();
	List<String> invitations();

	void clearFields();

	
}
