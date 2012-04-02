package agitter.domain.events;

import java.util.List;
import java.util.Set;

import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;

@Deprecated
@SuppressWarnings("unused")
public class EventImpl implements Event {
	String _description;
	long _datetime;
	User _owner;
	
	private Set<Group> groupInvitations;
	private Set<EmailAddress> emailInvitations;
	
	private Set<User> notInterested;

	
	@Override
	public String description() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public long[] datetimes() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public User[] invitees() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	public long id() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	@Override
	public User owner() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void notInterested(User user) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public Group[] groupInvitees() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void addInvitee(User invitee) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void removeInvitee(User invitee) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void addInvitee(Group invitee) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void removeInvitee(Group invitee) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public List<User> allResultingInvitees() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public void addDate(long date) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public void removeDate(long date) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	
	@Override
	public long getId() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public Occurrence[] occurrences() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public void notInterested(User user, long date) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public long[] interestedDatetimes(User user) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public long[] datetimesToCome() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public void going(User user, long date) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public void notGoing(User user, long date) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public void mayGo(User user, long date) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public Boolean isGoing(User user, long date) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public boolean hasIgnored(User user, long date) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public void changeDate(long from, long to) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public boolean isVisibleTo(User user) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void replace(User beingDropped, User receivingEvents) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public boolean isPublic() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}


	@Override
	public void setPublic(boolean publicEvent) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

}
