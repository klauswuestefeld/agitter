package agitter.ui.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.User;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.ui.presenter.SimpleTimer.HandleToAvoidLeaks;
import agitter.ui.view.EventData;
import agitter.ui.view.InviteView;
import agitter.ui.view.SessionView;

public class SessionPresenter {

	private final Events _events;
	private final User _user;
	private final SessionView _view;
	private final Consumer<String> _errorDisplayer;
	
	@SuppressWarnings("unused")
	private final HandleToAvoidLeaks _handle;

	
	public SessionPresenter(Events events, User user, SessionView view, Runnable onLogout, Consumer<String> errorDisplayer) {
		_events = events;
		_user = user;
		_view = view;
		_errorDisplayer = errorDisplayer;
		
		_view.onLogout(onLogout);
		
		inviteView().clearFields();
		inviteView().onInvite(new Runnable() { @Override public void run() {
			invite();
		}});

		_handle = SimpleTimer.runNowAndPeriodically(new Runnable(){  @Override public void run() {
			refreshEventList();
		}});
	}

	
	private void invite() {
		String description = inviteView().getEventDescription();
		Date datetime = inviteView().getDatetime();
		try {
			validate(datetime);
			_events.create(_user, description, datetime.getTime());
		} catch (Refusal e) {
			_errorDisplayer.consume(e.getMessage());
			return;
		}
		refreshEventList();
		inviteView().clearFields();
	}
	
	
	private void validate(Date datetime) throws Refusal {
		if (datetime == null)
			throw new Refusal("Data do agito deve ser preenchida.");
	}
	
	
	private InviteView inviteView() {
		return _view.inviteView();
	}
	
	
	synchronized
	private void refreshEventList() {
		_view.eventListView().refresh(eventDataList(), SimpleTimer.MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
	}

	
	private List<EventData> eventDataList() {
		List<EventData> result = new ArrayList<EventData>();
		SortedSet<Event> toHappen = _events.toHappen(_user);
		for (Event event : toHappen)
			result.add(new EventData(
				event.description(),
				event.datetime(),
				"everybody@agitter.com",
				removeAction(event)
			));
		return result;
	}


	private Runnable removeAction(final Event event) {
		return new Runnable() { @Override public void run() {
			_events.remove(_user, event);
			refreshEventList();
		}};
	}

}

