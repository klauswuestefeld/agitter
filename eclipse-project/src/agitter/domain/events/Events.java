package agitter.domain.events;

import java.util.List;

import org.prevayler.bubble.Transaction;

import basis.lang.exceptions.Refusal;

import agitter.domain.users.User;

public interface Events {

	@Transaction
	Event create(User user, String description, long datetime) throws Refusal;
	
	void transferEvents(User receivingEvents, User beingDropped);
	void delete(User user, Event event);

	Event get(long eventId);
	List<EventOcurrence> toHappen(User user);
	List<EventOcurrence> search(User user, String caseInsensitiveFragment);

	@Transaction
	InvitationToSendOut popInvitationToSendOut(); //Returns null if none to send.
	interface InvitationToSendOut {
		User invitee();
		Event event();
	}
	
}
