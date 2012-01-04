package agitter.ui.view.session.events;


public interface EventsView {

	void show();

	void onNewEvent(Runnable onNewEvent);
	
	EventView inviteView();
	EventListView initEventListView();

}
