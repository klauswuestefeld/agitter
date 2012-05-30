package agitter.domain.events;

import agitter.domain.events.Events.InvitationToSendOut;
import agitter.domain.users.User;

class InvitationToSendOutImpl implements InvitationToSendOut {

	private final User invitee;
	private final Event event;

	InvitationToSendOutImpl(User invitee, Event event) {
		this.invitee = invitee;
		this.event = event;
	}

	@Override
	public User invitee() {
		return invitee;
	}

	@Override
	public Event event() {
		return event;
	}

}
