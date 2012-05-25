package agitter.domain.events;

import static agitter.domain.events.Event.Attendance.NOT_GOING;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class EventImpl2 implements Event {
	
	private static final long ONE_HOUR = 1000 * 60 * 60;
	private static final long TWO_HOURS = ONE_HOUR * 2;

	private final long id;

	private Invitation invitationTree;
	
	private String _description;
	@SuppressWarnings("unused") @Deprecated private long _datetime;
	@SuppressWarnings("unused") @Deprecated private long[] datetimes;
	
	private Set<OccurrenceImpl> occurrences = new HashSet<OccurrenceImpl>();
	
	final private Set<User> notInterested = new HashSet<User>();
	
	public EventImpl2(long id, User owner, String description, long datetime) throws Refusal {
		this.id = id;
		if (null == owner) { throw new IllegalArgumentException("user cannot be null"); }
		invitationTree = new InvitationImpl(owner);
		edit(description, datetime);
	}

	@Override
	public User owner() {
		return invitationTree.host();
	}
	
	@Override
	public String description() {
		return _description;
	}

	@Override
	public long[] datetimes() {
		long[] ret = new long[actualOccurrences().size()];
		int cont=0;
		for (OccurrenceImpl occ : actualOccurrences()) {
			ret[cont++] = occ.datetime();
		}
		return ret;
	}

	private Set<OccurrenceImpl> actualOccurrences() {
		if (occurrences==null) occurrences = new HashSet<OccurrenceImpl>();
		return occurrences;
	}
	
	@Override
	public void setNotInterested(User user) {
		if(isOwner(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		if(!isInvited(user)) throw new IllegalArgumentException( "Não convidados não podem deixar de se interessar." );
		
		notInterested.add(user);
	}
	
	
	@Override
	public boolean isVisibleTo(User user) {
		if (isOwner(user)) return true;
		return isInvited(user);
	}

	
	@Override
	public boolean isInterested(User user) {
		return !notInterested.contains(user);
	}
	
	private boolean isInvited(User user) {
		return invitationTree.isInvited(user);
	}

	private void assertIsInTheFuture(long datetime) throws Refusal {
		if (datetime < Clock.currentTimeMillis() - ONE_HOUR)
			throw new Refusal("Agito no passado??!?");
	}


	void edit(String newDescription, long newDatetime) throws Refusal {
		if (null == newDescription) { throw new Refusal("Descrição do agito deve ser preenchida."); }
		assertIsInTheFuture(newDatetime);

		_description = newDescription;
		actualOccurrences().clear();
		addDate(newDatetime);
	}

	@Override
	public void invite(User host, User invitee) {
		if (!invitationTree.invite(host, invitee))
			throw new IllegalArgumentException("Host " + host + " não é convidado");
	}
	
	@Override
	public void invite(User host, Group invitees) {
		if (!invitationTree.invite(host, invitees)) {
			throw new IllegalArgumentException("Host " + host + " não é convidado");
		}
	}
	
	@Override
	public void uninvite(User invitee) {
		invitationTree.removeInvitee(invitee);
	}
	
	@Override
	public void uninvite(Group invitee) {
		invitationTree.removeInvitee(invitee);
	}
	
	
	@Override
	public void addDate(long datetime) {
		actualOccurrences().add(new OccurrenceImpl(datetime, this.owner()));
	}

	public OccurrenceImpl searchOccurrence(long datetime) {
		for (OccurrenceImpl occ : occurrences)
			if (occ.datetime() == datetime) return occ;
		return null;
	}
	
	@Override
	public void removeDate(long datetime) {		
		OccurrenceImpl toRemove = searchOccurrence(datetime);
		if (toRemove != null)
			actualOccurrences().remove(toRemove);
	}

	@Override
	public void changeDate(long from, long to) {
		removeDate(from);
		addDate(to);
	}


	@Override
	public User[] allResultingInvitees() {
		Set<User> result = invitationTree.allResultingInvitees();
		result.remove(invitationTree.host());
		return result.toArray(new User[result.size()]);
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public long[] datetimesToCome() {
		return datetimesToComeFilteredBy(null);
	}

	long[] datetimesToComeFilteredBy(User user) {
		final long start = twoHoursAgo();

		int count = 0;
		long[] ret = new long[actualOccurrences().size()];
		for (OccurrenceImpl occ : actualOccurrences()) {
			if (occ.datetime() < start) continue;
			if (user != null && occ.attendance(user) == NOT_GOING) continue;
			ret[count++] = occ.datetime();
		}

		return Arrays.copyOf(ret, count);
	}



	static private long twoHoursAgo() {
		return Clock.currentTimeMillis() - TWO_HOURS;
	}

	private void giveOwnershipTo(User newOwner) {
		invitationTree.transferHostTo(newOwner);
	}

	private boolean isOwner(User user) {
		return owner().equals(user);
	}
	
	@Override
	public void transferOwnershipIfNecessary(User takingOver, User beingDropped) {
		if (isOwner(beingDropped)) {
			giveOwnershipTo(takingOver);
			return;
		}
		
		User u = invitationTree.userThatInvited(beingDropped);
		if (u != null) 
			invite(u, takingOver);
			
		if (!isInterested(beingDropped)) 
			try {
				setNotInterested(takingOver);	
			} catch (Exception e) {} // Do not need to handle exception. It is ok if it happens. 
			
		for (OccurrenceImpl occ : actualOccurrences()) 
			occ.copyBehavior(beingDropped, takingOver);
	}


	@Override
	public Invitation invitationTree() {
		return invitationTree;
	}
	
	@Override
	public String toString() {
		return description();
	}

	@Override
	public Attendance attendance(User user, long date) {
		return searchOccurrence(date).attendance(user);
	}

	@Override
	public void setAttendance(User user, long date, Attendance att) {
		searchOccurrence(date).setAttendance(user, att);
	}

	@Override
	public boolean isEditableBy(User user) {
		return this.owner() == user;
	}

	@Override
	public void setDescription(User user, String newDescription) throws Refusal {
		if (!isEditableBy(user)) throw new IllegalStateException("Event not editable by this user.");
		if (null == newDescription) { throw new Refusal("Descrição do agito deve ser preenchida."); }
		_description = newDescription;
	}
	
}
