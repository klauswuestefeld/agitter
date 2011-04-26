package agitter.ui.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.AgitterSession;
import agitter.domain.events.Event;
import agitter.ui.view.EventData;
import agitter.ui.view.InviteView;
import agitter.ui.view.SessionView;

public class SessionPresenter {

	private final AgitterSession _session;
	private final SessionView _view;
	private final Consumer<String> _errorDisplayer;

	
	public SessionPresenter(AgitterSession session, SessionView view, Consumer<String> errorDisplayer) {
		_session = session;
		_view = view;
		_errorDisplayer = errorDisplayer;
		
		inviteView().clearFields();
		inviteView().onInvite(new Runnable() { @Override public void run() {
			invite();
		}});

		refreshEventList();
	}

	
	private void invite() {
		String description = inviteView().getEventDescription();
		Date datetime = inviteView().getDatetime();
		try {
			validate(datetime);
			_session.events().create(description, datetime.getTime());
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
	
	
	private void refreshEventList() {
		_view.eventListView().display(eventDataList());
	}

	
	private List<EventData> eventDataList() {
		List<EventData> result = new ArrayList<EventData>();
		SortedSet<Event> toHappen = _session.events().toHappen();
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
			_session.events().remove(event);
			refreshEventList();
		}};
	}

}

