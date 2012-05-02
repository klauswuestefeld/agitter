package agitter.domain.events;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {

	User owner();
	Invitation invitationTree();

	String description();
	
	long[] datetimesInterestingFor(User user);
	long[] datetimes();
	long[] datetimesToCome();
	
	Occurrence[] occurrences();
	void addDate(long date);
	void removeDate(long date);
	void changeDate(long from, long to);

	void invite(User host, User invitee);
	void invite(User host, Group friends);
	
	void uninvite(User invitee);
	void uninvite(Group invitee);
	
	void notInterested(User user);
	void notInterested(User user, long date);

	User[] allResultingInvitees();

	long getId();
	
	void going(User user, long date);
	void notGoing(User user, long date);
	void mayGo(User user, long date);
	Boolean isGoing(User user, long date);
	boolean hasIgnored(User user, long date);

	boolean isVisibleTo(User user);

	void replace(User beingDropped, User receivingEvents);


}
