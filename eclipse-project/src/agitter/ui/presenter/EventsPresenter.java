package agitter.ui.presenter;

import static agitter.domain.events.Event.Attendance.GOING;
import static agitter.domain.events.Event.Attendance.MAYBE;
import static agitter.domain.events.Event.Attendance.NOT_GOING;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import agitter.domain.events.EventOcurrence;
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
import agitter.ui.view.session.events.EventsView;

public class EventsPresenter implements Boss, EventsView.Boss {

	private static final int MAX_EVENTS_TO_SHOW = 40;
	
	private final User user;
	private final ContactsOfAUser contacts;
	private final Events events;
	private final Comments comments;
	private final Consumer<String> warningDisplayer;
	private final EventsView view;
	private InvitePresenter invitePresenter;
	private EventListView eventListView;
	private Event eventSelected;
	private String searchFragment = "";

	@SuppressWarnings("unused")
	private final HandleToAvoidLeaks handle;
	private final Functor<EmailAddress, User> userProducer;


	public EventsPresenter(User user, ContactsOfAUser contacts, Events events, Comments comments, Functor<EmailAddress, User> userProducer, EventsView eventsView, Consumer<String> warningDisplayer, Notifier<String> urlRestPathNotifier) {
		this.user = user;
		this.contacts = contacts;
		this.events = events;
		this.comments = comments;
		this.userProducer = userProducer;
		this.view = eventsView;
		this.warningDisplayer = warningDisplayer;
		this.eventSelected = null;
		urlRestPathNotifier.addConsumerAndNotifyLastValue(new Consumer<String>() {
			@Override
			public void consume(String value) {
				tryToSelectEventFromUrl(value);
			}
		});

		handle = SimpleTimer.runNowAndPeriodically(new Runnable() { @Override public void run() {
			periodicRefresh();
		}});
		
		this.view.onNewEvent(new Runnable() { @Override public void run() {
			onNewEvent();
		}});
		
		eventsView.startRepontingTo(this);
		
		refreshContactsToChoose();
	}
	
	private void tryToSelectEventFromUrl(String value) {
		Long possibleId = tryToParseId(value);
		if(possibleId != null) {
			Event possibleEvent = events.get(possibleId);
			if(possibleEvent != null)
				if(possibleEvent.isVisibleTo(user))
					selectEvent(possibleEvent);
				else 
					warningDisplayer.consume("Você não possui acesso ao agito!");	
			else
				warningDisplayer.consume("Agito não encontrado.");
		}
	}
	
	private Long tryToParseId(String string) {
		try {
			if(string != null && !string.isEmpty())
				return Long.parseLong(string, 36);
		} catch(NumberFormatException e) { return null; }
		return null;
	}


	private void periodicRefresh() {
		refreshEventList();
		invitePresenter().periodicRefresh();
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
		List<EventVO> eventsVOs = searchFragment.isEmpty()
			? eventsToHappen() : search();
		eventsListView().refresh(eventsVOs, SimpleTimer.MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
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
		return asEventVOs(events.toHappen(user), MAX_EVENTS_TO_SHOW);
	}
	private List<EventVO> search() {
		return asEventVOs(events.search(user, searchFragment), Integer.MAX_VALUE);
	}

	private List<EventVO> asEventVOs(List<EventOcurrence> eventsList, int maximumNumberOfEvents) {
		List<EventVO> result = new ArrayList<EventVO>();
		for (EventOcurrence occurrence : eventsList) {
			result.add(asValueObject(occurrence));
			if (result.size() == maximumNumberOfEvents) break;
		}
		return result;
	}

	private EventVO asValueObject(EventOcurrence occ) {
		Event event = occ.event();

		User[] invitees = event.allResultingInvitees();
		return new EventVO(event, event.description(),
			occ.datetime(), event.owner().screenName(),
			event.isEditableBy(user),
			invitees.length, 
			uniqueGroupOrUserInvited(invitees), 
			isUniqueUserInvited(invitees), 
			event.attendance(user, occ.datetime()) == GOING);
	}
	
		
	private String uniqueGroupOrUserInvited(User[] invitees) {
		if (invitees.length != 1)
			return null;
		
		return invitees[0].email().toString();
	}


	private boolean isUniqueUserInvited(User[] invitees) {
		return (invitees.length == 1);
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
	public void onEventRemoved(Object removedEvent, long datetime) {
		Event event = (Event)removedEvent;
		if (event.isEditableBy(user)) {
			// This option is disabled on screen
			// Should never come here.
			System.out.println("Should NEVER come here");
			events.delete(user, event); 
		} else {
			System.out.println("Not interesting anymore");
			event.setAttendance(user, datetime, NOT_GOING);
		}
		
		selectEvent(null);
		refreshEventList();
		invitePresenter().clear();
	}
	
	@Override
	public void goingOnEvent(Object eventObject, long datetime) {
		Event event = (Event)eventObject;
		if (event.isEditableBy(user)) {
			// This option is disabled on screen
			// Should never come here.
			System.out.println("Should NEVER come here");
		} else {
			event.setAttendance(user, datetime, GOING);
		}
		selectEvent(null);
		refreshEventList();
		invitePresenter().clear();
	}


	@Override
	public void mayGoToEvent(Object eventObject, long datetime) {
		Event event = (Event)eventObject;
		if (event.isEditableBy(user)) {
			// This option is disabled on screen
			// Should never come here.
			System.out.println("Should NEVER come here");
		} else {
			System.out.println("Not interesting anymore");
			event.setAttendance(user, datetime, MAYBE);
		}
		selectEvent(null);
		refreshEventList();
		invitePresenter().clear();
	}

	
	public void setUpdateContactsListener(Runnable runnable) {
		invitePresenter().setUpdateContactsListener(runnable);
	}

	
	@Override
	public void onSearch(String fragment) {
		if (fragment == null) fragment = "";
		searchFragment = fragment.trim();
		refreshEventList();
	}
}
