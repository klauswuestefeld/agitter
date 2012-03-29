package agitter.domain.events;

import java.util.List;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {

	User owner();

	String description();
	
	long[] interestedDatetimes(User user);
	long[] datetimes();
	long[] datetimesToCome();
	
	Occurrence[] occurrences();
	void addDate(long date);
	void removeDate(long date);
	void changeDate(long from, long to);

	User[] invitees();
	void addInvitee(User invitee);
	void removeInvitee(User invitee);
	Group[] groupInvitees();
	void addInvitee(Group invitee);
	void removeInvitee(Group invitee);

	void notInterested(User user);
	void notInterested(User user, long date);

	List<User> allResultingInvitees();

	long getId();
	
	boolean isPublic();
	void setPublic(boolean publicEvent);

	void going(User user, long date);
	void notGoing(User user, long date);
	void mayGo(User user, long date);
	Boolean isGoing(User user, long date);
	boolean hasIgnored(User user, long date);

	boolean isVisibleTo(User user);

	void replace(User beingDropped, User receivingEvents);
}
