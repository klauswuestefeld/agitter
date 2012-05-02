package agitter.domain.events;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {

	enum Attendance { GOING, MAYBE, NOT_GOING }

	User owner();
	Invitation invitationTree();

	String description();
	
	Attendance attendance(User ana, long date);
	void setAttendance(User ana, long date, Attendance att);

	void notInterested(User user);
	long[] datetimesInterestingFor(User user);
	long[] datetimes();
	long[] datetimesToCome();
	
	void addDate(long date);
	void removeDate(long date);
	void changeDate(long from, long to);

	void invite(User host, User invitee);
	void invite(User host, Group friends);
	
	void uninvite(User invitee);
	void uninvite(Group invitee);
	

	User[] allResultingInvitees();

	long getId();
	

	boolean isVisibleTo(User user);

	void replace(User beingDropped, User receivingEvents);


}
