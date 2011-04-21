package agitter.ui.view;

public interface AgitterView {

	InviteView inviteView();
	
	EventListView eventListView();
	
	void showErrorMessage(String message);
}
