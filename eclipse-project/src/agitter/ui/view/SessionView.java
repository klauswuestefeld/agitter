package agitter.ui.view;

public interface SessionView {

	InviteView inviteView();
	
	EventListView eventListView();

	void show(String username);
	
	void onLogout(Runnable onLogout);
}
