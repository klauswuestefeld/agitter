package agitter.ui.presenter;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.comments.Comments;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.presenter.SimpleTimer.HandleToAvoidLeaks;
import agitter.ui.view.session.events.EventListView;
import agitter.ui.view.session.events.EventListView.Boss;
import agitter.ui.view.session.events.EventVO;
import agitter.ui.view.session.events.EventVOComparator;
import agitter.ui.view.session.events.EventsView;

public class EventsPresenter implements Boss {

	private static final long TWO_HOURS = 1000 * 60 * 60 * 2;
	
	private final User user;
	private final ContactsOfAUser contacts;
	private final Events events;
	private final Comments comments;
	private final Consumer<String> warningDisplayer;
	private final EventsView view;
	private InvitePresenter invitePresenter;
	private EventListView eventListView;
	private Event eventSelected;

	@SuppressWarnings("unused")
	private final HandleToAvoidLeaks handle;
	private final Functor<EmailAddress, User> userProducer;

	
	public EventsPresenter(User user, ContactsOfAUser contacts, Events events, Comments comments, Functor<EmailAddress, User> userProducer, EventsView eventsView, Consumer<String> warningDisplayer) {
		this.user = user;
		this.contacts = contacts;
		this.events = events;
		this.comments = comments;
		this.userProducer = userProducer;
		this.view = eventsView;
		this.warningDisplayer = warningDisplayer;
		
		this.eventSelected = null;

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
		selectEvent(event);
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


	private void onEventChange() {
		refreshEventList();
	}


	private InvitePresenter invitePresenter() {
		if (invitePresenter == null)
			invitePresenter = new InvitePresenter(user, contacts, events, comments, userProducer, view.inviteView(), warningDisplayer, new Runnable() { @Override public void run() {
				onEventChange();
			}});

		return invitePresenter;
	}

	
	synchronized private void refreshEventList() {
		eventsListView().refresh(eventsToHappen(), SimpleTimer.MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
		eventsListView().setSelectedEvent(eventSelected);

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
		
		// TWO hours ago mesmo na lista? 
		final long twoHoursAgo = Clock.currentTimeMillis() - TWO_HOURS;
		
		for (Event event : toHappen) {
			for (long date : event.datetimes())
				if (date > twoHoursAgo)
					result.add(new EventVO(event, event.description(), 
						date, event.owner().screenName(), 
						events.isEditableBy(user, event),
						event.allResultingInvitees().size(), 
						uniqueGroupOrUserInvited(event), 
						isUniqueUserInvited(event)));

			// This happens when changing an event with only one date. 
			// the system removes the last date and includes a new one
			// in the middle of these events, this function is called. 
			if (event.datetimes().length == 0) 
				result.add(new EventVO(event, event.description(), 
						0, event.owner().screenName(), 
						events.isEditableBy(user, event),
						event.allResultingInvitees().size(), 
						uniqueGroupOrUserInvited(event), 
						isUniqueUserInvited(event)));
				
		}
		
		Collections.sort(result, new EventVOComparator());
		
		return result;
	}
	
		
	private String uniqueGroupOrUserInvited(Event event) {
		if (event.invitees().length + event.groupInvitees().length != 1)
			return null;
		return event.invitees().length > 0 ? 
				event.invitees()[0].email().toString() : 
				event.groupInvitees()[0].name(); 
	}


	private boolean isUniqueUserInvited(Event event) {
		return (event.invitees().length == 1 && event.groupInvitees().length == 0);
	}


	private void selectEvent(Event eventSelected) {
		this.eventSelected = eventSelected;
		eventsListView().setSelectedEvent(this.eventSelected);
		invitePresenter().setSelectedEvent(this.eventSelected);
	}
	

	@Override
	public void onEventSelected(Object eventSelected) {
		selectEvent((Event)eventSelected);
	}


	@Override
	public void onEventRemoved(Object removedEvent) {
		Event event = (Event)removedEvent;
		if (events.isEditableBy(user, event))
			events.delete(user, event);
		else
			event.notInterested(user);
		
		eventSelected = null;
		refreshEventList();
		invitePresenter().clear();
	}

}
