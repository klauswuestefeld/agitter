package agitter.domain.events;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;

public class EventImpl implements Event {

	public EventImpl(User owner, String description, long datetime, List<Group> inviteeGroups, List<EmailAddress> inviteeEmails) throws Refusal {
		if(null==owner) { throw new IllegalArgumentException("user cannot be null"); }
		if(datetime==0L) { throw new Refusal("Data do agito deve ser preenchida."); }
		if(null==description) { throw new Refusal("Descrição do agito deve ser preenchida."); }
		_owner = owner;
		_description = description;
		_datetime = datetime;
		addGroupInviteesIfAny(inviteeGroups);
		addEmailInviteesIfAny(inviteeEmails);
	}


	private void addEmailInviteesIfAny(List<EmailAddress> inviteeEmails) {
		for (EmailAddress email : inviteeEmails)
			addInvitee(email);
	}


	private void addGroupInviteesIfAny(List<Group> inviteeGroups) {
		for (Group group : inviteeGroups)
			addInvitees(group);
	}


	final private String _description;
	final private long _datetime;
	final private User _owner;
	
	private Set<Group> groupInvitations = new HashSet<Group>();
	private Set<EmailAddress> emailInvitations = new HashSet<EmailAddress>();
	
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
	public void addInvitees(Group group) {
		groupInvitations().add(group);
	}


	@Override
	public void addInvitee(EmailAddress emailAddress) {
		emailInvitations().add(emailAddress);
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
		EmailAddress mail = mailAddress(user);
		return emailInvitations().contains(mail) || groupInvitationsContain(mail);
	}


	private boolean groupInvitationsContain(EmailAddress mail) {
		for (Group group : groupInvitations())
			if (group.deepContains(mail))
				return true;
		return false;
	}


	private EmailAddress mailAddress(User user) {
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

	
	synchronized
	private Set<EmailAddress> emailInvitations() {
		if (emailInvitations==null) emailInvitations = new HashSet<EmailAddress>();
		return emailInvitations;
	}

	
	synchronized
	private Set<Group> groupInvitations() {
		if (groupInvitations==null) groupInvitations = new HashSet<Group>();
		return groupInvitations;
	}
	

}
