package agitter.ui.presenter.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import agitter.domain.Agitter;
import agitter.domain.Event;
import agitter.ui.view.AgitterView;
import agitter.ui.view.EventData;
import agitter.ui.view.InviteView;

class PresenterSession {

	private final Agitter _agitter;
	private final AgitterView _view;

	
	PresenterSession(Agitter agitter, AgitterView view) {
		_agitter = agitter;
		_view = view;
		
		inviteView().onInvite(inviteAction());
		inviteView().clearFields();

		refreshEventList();
	}

	
	private Runnable inviteAction() {
		return new Runnable() { @Override public void run() {
			String description = inviteView().getEventDescription();
			Date datetime = inviteView().getDatetime();
			try {
				validate(datetime);
				_agitter.events().create(description, datetime.getTime());
				refreshEventList();
				inviteView().clearFields();
			}
			catch(Exception e) {
				_view.showErrorMessage(e.getMessage());
			}
		}};
	}

	
	private void validate(Date datetime) {
		//TODO: Should be in model
		if (datetime == null)
			throw new IllegalArgumentException("Data do agito deve ser preenchida.");
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

