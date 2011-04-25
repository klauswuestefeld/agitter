package agitter.ui.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.Agitter;
import agitter.domain.Event;
import agitter.ui.view.AgitterView;
import agitter.ui.view.EventData;
import agitter.ui.view.InviteView;

public class Presenter {

	private final Agitter _agitter;
	private final AgitterView _view;

	
	public Presenter(Agitter agitter, AgitterView view) {
		_agitter = agitter;
		_view = view;
		
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
			_agitter.events().create(description, datetime.getTime());
		} catch (Refusal e) {
			_view.showErrorMessage(e.getMessage());
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
		SortedSet<Event> toHappen = _agitter.events().toHappen();
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
			_agitter.events().remove(event);
			refreshEventList();
		}};
	}

}

