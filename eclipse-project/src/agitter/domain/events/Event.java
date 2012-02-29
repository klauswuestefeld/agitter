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

	User[] invitees();
	void addInvitee(User invitee);
	void removeInvitee(User invitee);
	Group[] groupInvitees();
	void addInvitee(Group invitee);
	void removeInvitee(Group invitee);

	void notInterested(User user);
	void notInterested(User user, long date);

	

	List<User> allResultingInvitees();
	
	@Deprecated
	long getId();
	@Deprecated
	void setId(long id);
	
}
