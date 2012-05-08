package agitter.ui.view.session.events;


public interface EventsView {
	
	interface Boss {
		void onSearch(String fragment);
	}
	
	public void startRepontingTo(Boss boss);


	void show();

	void onNewEvent(Runnable onNewEvent);
	
	EventView inviteView();
	EventListView initEventListView();

}
