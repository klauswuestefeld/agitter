package agitter.ui.view.session.events;


public interface EventsView {

	void show();

	void onNewEvent(Runnable onNewEvent);
	
	EventView showInviteView();
	EventListView initEventListView();

}
