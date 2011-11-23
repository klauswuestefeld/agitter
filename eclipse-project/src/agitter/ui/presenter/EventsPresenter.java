package agitter.ui.presenter;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.presenter.SimpleTimer.HandleToAvoidLeaks;
import agitter.ui.view.session.events.EventListView;
import agitter.ui.view.session.events.EventListView.Boss;
import agitter.ui.view.session.events.EventVO;
import agitter.ui.view.session.events.EventsView;

public class EventsPresenter implements Boss {

	private final User user;
	private final ContactsOfAUser contacts;
	private final Events events;
	private final Consumer<String> warningDisplayer;
	private final EventsView view;
	private InvitePresenter invitePresenter;
	private EventListView eventListView;

	@SuppressWarnings("unused")
	private final HandleToAvoidLeaks handle;
	private final Functor<EmailAddress, User> userProducer;

	
	public EventsPresenter(User user, ContactsOfAUser contacts, Events events, Functor<EmailAddress, User> userProducer, EventsView eventsView, Consumer<String> warningDisplayer) {
		this.user = user;
		this.contacts = contacts;
		this.events = events;
		this.userProducer = userProducer;
		this.view = eventsView;
		this.warningDisplayer = warningDisplayer;

		handle = SimpleTimer.runNowAndPeriodically(new Runnable() { @Override public void run() {
			refreshEventList();
		}});
		
		this.view.onNewEvent(new Runnable() { @Override public void run() {
			onNewEvent();
		}});
		
		refreshContactsToChoose();
	}


	void refreshContactsToChoose() {
		invitePresenter().refreshContactsToChoose();
	}

	
	private void onNewEvent() {
		Event event;
		try {
			event = events.create(user, "", suggestedTime());
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
			return;
		}
		refreshEventList();
		invitePresenter().setSelectedEvent(event);
	}


	private long suggestedTime() {
		GregorianCalendar result = new GregorianCalendar();
		result.setTimeInMillis(Clock.currentTimeMillis());
		result.clear(MINUTE);
		result.clear(SECOND);
		result.clear(MILLISECOND);
		
		setHourToEightAtNightOrNextFullHour(result);
		
		return result.getTimeInMillis();
	}


	private void setHourToEightAtNightOrNextFullHour(GregorianCalendar cal) {
		if (cal.get(HOUR_OF_DAY) < 20)
			cal.set(HOUR_OF_DAY, 20);
		else
			cal.add(HOUR_OF_DAY, 1);
	}


	private void onDateOrDescriptionChange() {
		refreshEventList();
	}


	private InvitePresenter invitePresenter() {
		if (invitePresenter == null)
			invitePresenter = new InvitePresenter(user, contacts, events, userProducer, view.showInviteView(), warningDisplayer, new Runnable() { @Override public void run() {
				onDateOrDescriptionChange();
			}});

		return invitePresenter;
	}

	
	synchronized private void refreshEventList() {
		eventsListView().refresh(eventsToHappen(), SimpleTimer.MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
	}

	
	private EventListView eventsListView() {
		if (eventListView == null) {
			eventListView = view.initEventListView();
			eventListView.startReportingTo(this);
		}

		return eventListView;
	}

	
	private List<EventVO> eventsToHappen() {
		List<EventVO> result = new ArrayList<EventVO>();
		List<Event> toHappen = events.toHappen(user);
		for (Event event : toHappen)
			result.add(new EventVO(event, event.description(), event.datetime(), event.owner().screenName(), isDeletable(event)));
		return result;
	}

	
	private boolean isDeletable(Event event) {
		return events.isEditableBy(user, event);
	}


	@Override
	public void onEventSelected(Object selectedEvent) {
		invitePresenter().setSelectedEvent(((Event)selectedEvent));
	}


	@Override
	public void onEventRemoved(Object removedEvent) {
		Event event = (Event)removedEvent;
		if (isDeletable(event))
			events.delete(user, event);
		else
			event.notInterested(user);
		
		refreshEventList();
		invitePresenter().clear();
	}

}
