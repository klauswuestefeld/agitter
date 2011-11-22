package agitter.ui.presenter;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.presenter.SimpleTimer.HandleToAvoidLeaks;
import agitter.ui.view.session.events.EventListView;
import agitter.ui.view.session.events.EventVO;
import agitter.ui.view.session.events.EventsView;

public class EventsPresenter {

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
		
		this.view.onNewEvent(new Runnable() { public void run() {
			onNewEvent();
		}});
		
		refreshContactsToChoose();
	}


	void refreshContactsToChoose() {
		invitePresenter().refreshContactsToChoose();
	}

	
	private void onNewEvent() {
		invitePresenter().startCreatingNewEvent();
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
		if (eventListView == null)
			eventListView = view.initEventListView(new Consumer<Object>() { @Override public void consume(Object event) {
				onEventSelected((Event)event);
			}}, new Consumer<Object>() { @Override public void consume(Object event) {
				onEventRemoved((Event)event);
			}});

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


	private void onEventSelected(Event event) {
		invitePresenter().setSelectedEvent(event);
	}

	
	private void onEventRemoved(Event event) {
		if (isDeletable(event))
			events.delete(user, event);
		else
			event.notInterested(user);
		
		refreshEventList();
		invitePresenter().clear();
	}

}
