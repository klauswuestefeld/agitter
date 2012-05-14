package agitter.domain.events;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {

	enum Attendance { GOING, MAYBE, NOT_GOING }

	User owner();
	String description();
	boolean isVisibleTo(User user);
	
	Attendance attendance(User user, long date);
	void setAttendance(User user, long date, Attendance att);

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
