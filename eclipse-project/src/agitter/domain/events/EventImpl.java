package agitter.domain.events;


import java.util.HashSet;
import java.util.Set;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;

public class EventImpl implements Event {

	public static boolean PRIVATE_EVENTS_ON = false;
	

	public EventImpl(User owner, String description, long datetime) {
		if(null==owner) { throw new IllegalArgumentException("user can not be null"); }
		if(datetime==0L) { throw new IllegalArgumentException("Data do agito deve ser preenchida."); }
		if(null==description) { throw new IllegalArgumentException("description can not be null"); }
		_owner = owner;
		_description = description;
		_datetime = datetime;
	}

	
	final private String _description;
	final private long _datetime;
	final private User _owner;
	
	final private Set<Group> groupInvitations = new HashSet<Group>();
	final private Set<EmailAddress> emailInvitations = new HashSet<EmailAddress>();
	
	final private Set<User> notInterested = new HashSet<User>();

	
	@Override
	public String description() {
		return _description;
	}

	
	@Override
	public long datetime() {
		return _datetime;
	}

	
	@Override
	public User owner() {
		return _owner;
	}

	
	@Override
	public void addInvitation(Group group) {
		groupInvitations.add(group);
	}


	@Override
	public void addInvitation(EmailAddress emailAddress) {
		emailInvitations.add(emailAddress);
	}


	@Override
	public void notInterested(User user) {
		if(owner().equals(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		
		notInterested.add(user);
	}
	
	
	private boolean isInterested(User user) {
		return !notInterested.contains(user);
	}
	
	
	boolean isVisibleTo(User user) {
		if (owner().equals(user)) return true;
		return isInvited(user) && isInterested(user);
	}


	private boolean isInvited(User user) {
		if(! PRIVATE_EVENTS_ON) return true;
		EmailAddress mail = mail(user);

		return emailInvitations.contains(mail) || groupInvitationsContain(mail);
	}


	private boolean groupInvitationsContain(EmailAddress mail) {
		for (Group group : groupInvitations)
			if (group.deepContains(mail))
				return true;
		return false;
	}


	private EmailAddress mail(User user) {
		try {
			return new EmailAddress(user.email());
		} catch (Refusal e) {
			throw new IllegalStateException();
		}
	}

	
	@Override
	public boolean equals(Object o) {
		if(this==o) { return true; }
		if(o==null || getClass()!=o.getClass()) { return false; }

		EventImpl agito = (EventImpl) o;

		return _datetime==agito._datetime && _description.equals(agito._description);
	}

	
	@Override
	public int hashCode() {
		int result = (int) _datetime;
		result = 31*result+_description.hashCode();
		return result;
	}

	

}
