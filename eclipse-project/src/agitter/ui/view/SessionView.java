package agitter.ui.view;

public interface SessionView {

	InviteView inviteView();
	
	EventListView eventListView();

	void onLogout(Runnable onLogout);
}
