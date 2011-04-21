package agitter.ui.view;

import java.util.Date;


public interface InviteView {

	void onInvite(Runnable action);
	
	String getEventDescription();
	Date getDatetime();

	void clearFields();
	
}
