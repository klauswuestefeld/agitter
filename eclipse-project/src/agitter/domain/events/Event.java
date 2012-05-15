package agitter.domain.events;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {

	enum Attendance { GOING, MAYBE, NOT_GOING }

	User owner();
	boolean isEditableBy(User user);
	void setDescription(User user, String newDescription) throws Refusal;
	String description();
	boolean isVisibleTo(User user);
	
	void setAttendance(User user, long date, Attendance att);
	Attendance attendance(User user, long date);

	void setNotInterested(User user);
	boolean isInterested(User user);

	void addDate(long date);
	void removeDate(long date);
	void changeDate(long from, long to);
	long[] datetimesInterestingFor(User user);
	long[] datetimes();
	long[] datetimesToCome();

	Invitation invitationTree();
	void invite(User host, User invitee);
	void invite(User host, Group friends);
	void uninvite(User invitee);
	void uninvite(Group invitee);
	User[] allResultingInvitees();
	
	long getId();
	
	void transferOwnershipIfNecessary(User receivingEvents, User beingDropped);
}
