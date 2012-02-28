package agitter.ui.presenter;

import static infra.util.ToString.sortIgnoreCase;
import static infra.util.ToString.toStrings;
import infra.util.ToString;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.comments.Comment;
import agitter.domain.comments.Comments;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.emails.AddressValidator;
import agitter.domain.emails.EmailAddress;
import agitter.domain.emails.EmailExtractor;
import agitter.domain.emails.EmailExtractor.Visitor;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.presenter.SimpleTimer.HandleToAvoidLeaks;
import agitter.ui.view.session.events.EventView;

public class InvitePresenter implements EventView.Boss {

	private static final long TWO_HOURS = 1000 * 60 * 60 * 2;
	private static final DateFormat COMMENTS_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private final User user;
	private final ContactsOfAUser contacts;
	private final Events events;
	private final Comments comments;
	private final Consumer<String> warningDisplayer;
	private final Functor<EmailAddress, User> userProducer;
	private final EventView view;
	private final Runnable onEventDataChanged;
	@SuppressWarnings("unused")
	private final HandleToAvoidLeaks handle;


	private Event selectedEvent = null;

	
	InvitePresenter(User user, ContactsOfAUser contacts, Events events, Comments comments, Functor<EmailAddress, User> userProducer, EventView view, Consumer<String> warningDisplayer, Runnable onEventDataChanged) {
		this.user = user;
		this.contacts = contacts;
		this.events = events;
		this.comments = comments;
		this.userProducer = userProducer;
		this.view = view;
		this.warningDisplayer = warningDisplayer;
		this.onEventDataChanged = onEventDataChanged;
		handle = SimpleTimer.runNowAndPeriodically(new Runnable() { @Override public void run() {
			refreshCommentsList();
		}});


		this.view.startReportingTo(this);
		
		refreshContactsToChoose();
		refresh();
	}

	void setSelectedEvent(Event event) {
		selectedEvent = event;
		refresh();
	}

	
	void clear() {
		setSelectedEvent(null);
	}

	private long[] onlyFutureDates(long[] dates) {
		final long twoHoursAgo = Clock.currentTimeMillis() - TWO_HOURS;
		
		List<Long> futureDates = new ArrayList<Long>();
		
		for (long datetime : dates) { 
			if (datetime > twoHoursAgo)
				futureDates.add(datetime);
		}
		
		long[] ret = new long[futureDates.size()];
	    int i=0;
	    for (Long data : futureDates) {
	        ret[i] = data;
	        i++;
	    }
	    
		return (long[]) ret;
	}
	
	private void refresh() {
		if (selectedEvent == null) {
			view.clear();
			return;
		}
		
		if (events.isEditableBy(user, selectedEvent)) {
			view.displayEditting(
					selectedEvent.description(), 
					//onlyFutureDates(selectedEvent.datetimes()),
					selectedEvent.datetimes(),
					sortedInviteesOf(selectedEvent),
					selectedEvent.allResultingInvitees().size(),
					allComments());
		} else {
			view.displayReadOnly(
					selectedEvent.owner().email().toString(),
					selectedEvent.description(), 
					onlyFutureDates(selectedEvent.datetimes()),
					sortedKnownInviteesOf(selectedEvent),
					selectedEvent.allResultingInvitees().size(),
					allComments());
		}
	}

	private boolean commentsEnabled() {
		return EventView.COMMENTS_ENABLED;
	}
	private List<String> allComments() {
		if(!commentsEnabled() || selectedEvent==null) 
			return new ArrayList<String>();
		
		List<Comment> commentsFor = comments.commentsFor(selectedEvent);
		List<String> ret = new ArrayList<String>();
		for(Comment c: commentsFor) {
			ret.add( formatComment(c) );
		}
		return ret;
	}
	
	private String formatComment(Comment comment) {
		return "Em " + COMMENTS_DATE_FORMAT.format(new Date(comment.creationDatetime())) + " " + comment.owner().email() + " disse: " + comment.text() + "\n";
	}


	void refreshContactsToChoose() {
		view.refreshInviteesToChoose(contacts());
	}


	private List<String> sortedInviteesOf(Event event) {
		List<String> result = sortedGroupsInviteesOf(event);
			
		String[] users = toStrings(event.invitees());
		List<String> userList = Arrays.asList(users);
		sortIgnoreCase(userList);
		result.addAll(userList);
		
		return result;
	}
	

	private List<String> sortedKnownInviteesOf(Event event) {
		List<String> userList = new ArrayList<String>();
		for (User u : event.allResultingInvitees())
			if (contacts.isMyFriend(u.email())) 
				userList.add(u.toString());
		userList.remove(user.email().toString());
		
		sortIgnoreCase(userList);
		return userList;
	}


	private List<String> sortedGroupsInviteesOf(Event event) {
		List<String> result = new ArrayList<String>();
		String[] groups = toStrings(event.groupInvitees());
		result.addAll(Arrays.asList(groups));
		sortIgnoreCase(result);	
		return result;
	}
	
		
	@Override
	public void onDescriptionEdit(String newText) {
		if (newText != null &&  newText.trim().equals("")) {
			warningDisplayer.consume("Preencha a descrição do agito.");
			return;
		}
			
		try {
			events.setDescription(user, selectedEvent, newText);
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
			return;
		}
		onEventDataChanged.run();
	}

	@Override
	public boolean approveInviteesAdd(String invitees) {
		if (invitees == null) return false;

		if (selectedEvent == null) {
			warningDisplayer.consume("Preencha a data e a descrição do agito.");
			return false;
		}

		Object inviteeObj = toGroupOrUser(invitees);
		if (inviteeObj == null) {
			if (addMultipleInvitees(invitees)) {
				refresh();
				onEventDataChanged.run();
			} else
				warningDisplayer.consume("Grupo ou email invalido: " + invitees);

			return false;
		}
		
		if (inviteeObj instanceof Group)
			selectedEvent.addInvitee((Group)inviteeObj);
		else
			addInvitee((User)inviteeObj);

		onInvitationChanged();
		return true;
	}


	private void onInvitationChanged() {
		view.refreshInvitationsHeader(selectedEvent.allResultingInvitees().size());
		onEventDataChanged.run();
	}


	private boolean addMultipleInvitees(String invitees) {
		final AtomicBoolean wasAdded = new AtomicBoolean(false);
		EmailExtractor.extractAddresses(invitees, new Visitor() { @Override public void visit(String name, EmailAddress email) {
			wasAdded.set(true);
			User user = userProducer.evaluate(email);
			addInvitee(user);
			if (user.name() == null)
				user.setName(name);
		}});
		return wasAdded.get();
	}


	private void addInvitee(User user) {
		contacts.addContact(user);
		selectedEvent.addInvitee(user);
	}
	
	
	private Object toGroupOrUser(String invitee) {
		Object result = contacts.groupGivenName(invitee);
		if (result != null) return result;

		return AddressValidator.isValidEmail(invitee)
			? userProducer.evaluate(EmailAddress.certain(invitee))
			: null;
	}


	@Override
	public void onInviteeRemoved(String invitee) {
		Object inviteeObj = toGroupOrUser(invitee);
		if (inviteeObj == null) return;
		if (inviteeObj instanceof Group)
			selectedEvent.removeInvitee((Group)inviteeObj);
		else
			selectedEvent.removeInvitee((User)inviteeObj);
		onInvitationChanged();
	}

	@Override
	public void onDateRemoved(Long date) {
		selectedEvent.removeDate(date);
		onEventDataChanged.run();
	}
	
	@Override
	public void onDateAdded(Long date) {
		selectedEvent.addDate(date);
		onEventDataChanged.run();
	}
	
	private List<String> contacts() {
		List<String> contactsAndGroups = ToString.toStrings(contacts.groups());
		contactsAndGroups.addAll(ToString.toStrings(contacts.all()));
		return contactsAndGroups;
	}
	
	@Override
	public void onCommentPosted(String comment) {
		comments.commentOn(selectedEvent, user, comment);
		refresh();
	}


	@Override
	public void onEventRemoved() {
		if (events.isEditableBy(user, selectedEvent)) {
			// It is the owner of the event. Remove it. 
			events.delete(user, selectedEvent);
			System.out.println("Event Deleted");
		} else {
			// It is not the owner of the event. Remove it all. 
			selectedEvent.notInterested(user);
			System.out.println("Event Not Interested");
		}

		clear();
		refresh();
		onEventDataChanged.run();
	}
	
	protected void refreshCommentsList() {
		if(selectedEvent != null)
			view.refreshComments(allComments(), SimpleTimer.MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
	}

}


