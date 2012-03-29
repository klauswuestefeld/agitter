package agitter.ui.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.Pair;
import sneer.foundation.lang.exceptions.Refusal;
import vaadinutils.AutoCompleteChooser.FullFeaturedItem;
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
import agitter.ui.helper.ContactChooserHelper;
import agitter.ui.view.session.events.EventView;

public class InvitePresenter implements EventView.Boss {

	private final User user;
	private final ContactsOfAUser contacts;
	private final Events events;
	private final Consumer<String> warningDisplayer;
	private final Functor<EmailAddress, User> userProducer;
	private final EventView view;
	private final Runnable onEventDataChanged;


	private Event selectedEvent = null;

	private final CommentsPresenter commentsPresenter;

	
	InvitePresenter(User user, ContactsOfAUser contacts, Events events, Comments comments, Functor<EmailAddress, User> userProducer, EventView view, Consumer<String> warningDisplayer, Runnable onEventDataChanged) {
		this.user = user;
		this.contacts = contacts;
		this.events = events;
		this.userProducer = userProducer;
		this.view = view;
		this.warningDisplayer = warningDisplayer;
		this.onEventDataChanged = onEventDataChanged;

		this.view.startReportingTo(this);
		
		this.commentsPresenter = new CommentsPresenter(user, comments, this.view.commentsView());
		
		refreshContactsToChoose();
		clear();
	}

	void setSelectedEvent(Event event) {
		selectedEvent = event;
		refresh();
	}

	
	void clear() {
		setSelectedEvent(null);
	}

	void refresh() {
		commentsPresenter.setObject(selectedEvent);

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
					selectedEvent.isPublic());
		} else {
			view.displayReadOnly(
					getOwnerPair(selectedEvent),
					selectedEvent.description(), 
					selectedEvent.datetimesToCome(),
					sortedKnownInviteesOf(selectedEvent),
					selectedEvent.allResultingInvitees().size(), 
					selectedEvent.isPublic());
		}
	}


	void refreshContactsToChoose() {
		view.refreshInviteesToChoose(ContactChooserHelper.contacts(contacts.groups(), contacts.all()));
	}

	private List<FullFeaturedItem> sortedInviteesOf(Event event) {
		return ContactChooserHelper.contacts(event.groupInvitees(), event.invitees());
	}

	private List<FullFeaturedItem> sortedKnownInviteesOf(Event event) {
		List<User> userList = new ArrayList<User>();
		for (User u : event.allResultingInvitees()) {
			if (contacts.isMyFriend(u.email()) && !u.email().equals(user.email())) {
				userList.add(u);	
			}
		}
		
		return ContactChooserHelper.contacts(Collections.EMPTY_LIST, userList);
	}

	private Pair<String,String> getOwnerPair(Event selectedEvent) {
		return new Pair<String,String>(selectedEvent.owner().email().toString(), selectedEvent.owner().name());
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
	public void onOpennessChanged(boolean publicEvent) {
		selectedEvent.setPublic(publicEvent);
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
	
	@Override
	public void onDateChanged(Long from, Long to) {
		selectedEvent.changeDate(from,to);
		onEventDataChanged.run();
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
		onEventDataChanged.run();
	}
	
	
	void periodicRefresh() {
		commentsPresenter.periodicRefresh();
	}



}


