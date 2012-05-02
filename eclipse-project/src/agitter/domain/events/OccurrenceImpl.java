package agitter.domain.events;

import static agitter.domain.events.Event.Attendance.GOING;

import java.util.HashMap;
import java.util.Map;

import org.prevayler.bubble.PrevalentBubble;

import agitter.domain.events.Event.Attendance;
import agitter.domain.users.User;

class OccurrenceImpl {

	{
		PrevalentBubble.idMap().register(this);
	}
	
	private long datetime;
	private Map<User, Attendance> attendanceByUser = new HashMap<User, Attendance>();
	
	OccurrenceImpl(long datetime, User owner) {
		this.datetime = datetime;
		setAttendance(owner, GOING);
	}
	
	long datetime() {
		return datetime;
	}

	private Map<User,Attendance> attendanceByUser() {
		if (attendanceByUser == null) 
			attendanceByUser = new HashMap<User, Attendance>();
		return attendanceByUser;
	}

	void setAttendance(User user, Attendance att) {
		attendanceByUser().put(user, att);
	}


	Attendance attendance(User user) {	
		return attendanceByUser().get(user);
	}

	
	void copyBehavior(User leader, User sheep) {
		setAttendance(sheep, attendance(leader));
	}
}
