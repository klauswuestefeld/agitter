package agitter.ui.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.presenter.SimpleTimer.HandleToAvoidLeaks;
import agitter.ui.view.EventData;
import agitter.ui.view.InviteView;
import agitter.ui.view.SessionView;

public class SessionPresenter {

	private final Events _events;
	private final User _user;
	private final SessionView _view;
	private final Consumer<String> _warningDisplayer;

	@SuppressWarnings("unused")
	private final HandleToAvoidLeaks _handle;


	public SessionPresenter(Events events, User user, SessionView view, Runnable onLogout, Consumer<String> errorDisplayer) {
		_events = events;
		_user = user;
		_view = view;
		_warningDisplayer = errorDisplayer;

		_view.onLogout(onLogout);
		
		_view.show(_user.username()); 

		inviteView().clearFields();
		inviteView().onInvite(new Runnable() { @Override public void run() {
			invite();
		}});

		_handle = SimpleTimer.runNowAndPeriodically(new Runnable() { @Override public void run() {
			refreshEventList();
		}});
	}

	private void invite() {
		String description = inviteView().getEventDescription();
		Date datetime = inviteView().getDatetime();
		try {
			validate(datetime);
			_events.create(_user, description, datetime.getTime());
		} catch(Refusal e) {
			_warningDisplayer.consume(e.getMessage());
			return;
		}
		refreshEventList();
		inviteView().clearFields();
	}


	private void validate(Date datetime) throws Refusal {
		if(datetime==null) { throw new Refusal("Data do agito deve ser preenchida."); }
	}


	private InviteView inviteView() {
		return _view.inviteView();
	}


	synchronized
	private void refreshEventList() {
		_view.eventListView().refresh(eventsToHappen(), SimpleTimer.MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
	}


	private List<EventData> eventsToHappen() {
		List<EventData> result = new ArrayList<EventData>();
		List<Event> toHappen = _events.toHappen(_user);
		for(Event event : toHappen) {
			result.add(new EventData(
				event.description(),
				event.datetime(),
				event.owner().username(),
				removeAction(event)
			));
		}
		return result;
	}


	private Runnable removeAction(final Event event) {
		if (event.owner().equals(_user)) return null;
		
		return new Runnable() { @Override public void run() {
			event.notInterested(_user);
			refreshEventList();
		}};
	}

}

