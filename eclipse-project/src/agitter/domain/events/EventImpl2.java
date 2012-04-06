package agitter.domain.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class EventImpl2 implements Event {
	
	private static final long ONE_HOUR = 1000 * 60 * 60;
	private static final long TWO_HOURS = ONE_HOUR * 2;

	private final long id;

	private User _owner;
	private String _description;
	private boolean publicEvent;
	@Deprecated private long _datetime;
	@Deprecated private long[] datetimes;
	
	private Set<Occurrence> occurrences = new HashSet<Occurrence>();
	private Set<Group> groupInvitations = new HashSet<Group>();
	private Set<User> invitees = new HashSet<User>();
	
	final private Set<User> notInterested = new HashSet<User>();
	
	public EventImpl2(long id, User owner, String description, long datetime) throws Refusal {
		this.id = id;
		if(null==owner) { throw new IllegalArgumentException("user cannot be null"); }
		_owner = owner;
		edit(description, datetime);
	}


	@Override
	public User owner() {
		return _owner;
	}

	
	@Override
	public String description() {
		return _description;
	}

	@Override
	public long[] datetimes() {
		long[] ret = new long[actualOccurrences().size()];
		int cont=0;
		for (Occurrence occ : actualOccurrences()) {
			ret[cont++] = occ.datetime();
		}
		return ret;
		//return datetimes;
	}

	@Override
	public Occurrence[] occurrences() {
		return actualOccurrences().toArray(new Occurrence[actualOccurrences().size()]);
	}
	
	private Set<Occurrence> actualOccurrences() {
		if (occurrences==null) occurrences = new HashSet<Occurrence>();
		return occurrences;
	}
	
	@Override
	synchronized
	public User[] invitees() {
		return actualInvitees().toArray(new User[actualInvitees().size()]);
	}


	private Set<User> actualInvitees() {
		if (invitees==null) invitees = new HashSet<User>();
		return invitees;
	}

	
	@Override
	synchronized
	public Group[] groupInvitees() {
		return actualGroupInvitees().toArray(new Group[actualGroupInvitees().size()]);
	}


	private Set<Group> actualGroupInvitees() {
		if (groupInvitations==null) groupInvitations = new HashSet<Group>();
		return groupInvitations;
	}


	@Override
	public void notInterested(User user) {
		if(owner().equals(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		if(!invitees.contains(user)) throw new IllegalArgumentException( "Não convidados não podem deixar de se interessar." );
		
		notInterested.add(user);
	}
	
	@Override
	public void notInterested(User user, long date) {
		if(owner().equals(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		else if(!invitees.contains(user)) throw new IllegalArgumentException( "Não convidados não podem deixar de se interessar." );

		Occurrence o = searchOccurrence(date);
		if (o != null) {
			o.notInterested(user);
		}
	}
	
	@Override
	public void going(User user, long date) {
		Occurrence o = searchOccurrence(date);
		if(!owner().equals(user) && !invitees.contains(user)) throw new IllegalArgumentException( "Não convidados não podem participar." );
		
		if (o != null) {
			o.going(user);
		}
	}

	@Override
	public void notGoing(User user, long date) {
		if(owner().equals(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		else if(!invitees.contains(user)) throw new IllegalArgumentException( "Não convidados não podem deixar de se interessar." );

		Occurrence o = searchOccurrence(date);
		if (o != null) {
			o.notGoing(user);
		}
	}

	@Override
	public void mayGo(User user, long date) {
		if(owner().equals(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		else if(!invitees.contains(user)) throw new IllegalArgumentException( "Não convidados não podem deixar de se interessar." );

		Occurrence o = searchOccurrence(date);
		if (o != null) {
			o.mayGo(user);
		}
	}

	@Override
	public Boolean isGoing(User user, long date) {
		Occurrence o = searchOccurrence(date);
		if (o != null) {
			return o.isGoing(user);
		}
		return false;
	}
	
	@Override
	public boolean hasIgnored(User user, long date) {
		Occurrence o = searchOccurrence(date);
		if (o != null) {
			return o.hasIgnored(user);
		}
		return true;
	}
	
	@Override
	public boolean isVisibleTo(User user) {
		if (owner().equals(user)) return true;
		return isInvited(user) && isInterested(user);
	}

	
	private boolean isInterested(User user) {
		return !notInterested.contains(user);
	}
	
	public boolean isInvited(User user) {
		return actualInvitees().contains(user) || groupInvitationsContain(user);
	}

	private boolean groupInvitationsContain(User user) {
		for (Group group : actualGroupInvitees())
			if (group.deepContains(user))
				return true;
		return false;
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

	void edit(String newDescription, long[] newDatetimes) throws Refusal {
		if (null == newDescription) { throw new Refusal("Descrição do agito deve ser preenchida."); }

		_description = newDescription;
		actualOccurrences().clear();
		for (long l : newDatetimes) {
			addDate(l);
		}
	}
	
	@Override
	public void addInvitee(User invitee) {
		actualInvitees().add(invitee);
	}


	@Override
	public void removeInvitee(User invitee) {
		actualInvitees().remove(invitee);
	}
	
	@Override
	public void addDate(long datetime) {
		actualOccurrences().add(new OccurrenceImpl(datetime, this.owner()));
	}

	public Occurrence searchOccurrence(long datetime) {
		for (Occurrence occ : occurrences) {
			if (occ.datetime() == datetime) return occ;
		}
		return null;
	}
	
	@Override
	public void removeDate(long datetime) {		
		Occurrence toRemove = searchOccurrence(datetime);
		if (toRemove != null)
			actualOccurrences().remove(toRemove);
	}

	@Override
	public void changeDate(long from, long to) {
		removeDate(from);
		addDate(to);
	}

	@Override
	public void addInvitee(Group invitee) {
		actualGroupInvitees().add(invitee);
	}


	@Override
	public void removeInvitee(Group invitee) {
		actualGroupInvitees().remove(invitee);
	}


	@Override
	public List<User> allResultingInvitees() {
		Set<User> result = new HashSet<User>(invitees);
		for(Group g : groupInvitations)
			g.deepAddMembers(result);
		result.remove(_owner);
		return new ArrayList<User>(result);
	}


	void migrateSchemaIfNecessary() {
		// 2012-Fev-03
		if (datetimes == null) 
			datetimes = new long[]{_datetime};
		
		if (actualOccurrences().size() < datetimes.length) {
			actualOccurrences().clear();
			for (long l : datetimes) {
				addDate(l);
			}
		}
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public long[] interestedDatetimes(User user) {
		final long start = twoHoursAgo();

		int count = 0;
		long[] ret = new long[actualOccurrences().size()];
		for (Occurrence occ : actualOccurrences())
			if (occ.datetime() > start && occ.isInterested(user))
				ret[count++] = occ.datetime();
		
		return Arrays.copyOf(ret, count);
	}


	@Override
	public long[] datetimesToCome() {
		final long start = twoHoursAgo();

		int count = 0;
		long[] ret = new long[actualOccurrences().size()];
		for (Occurrence occ : actualOccurrences()) 
			if (occ.datetime() > start)
				ret[count++] = occ.datetime();
		
		return Arrays.copyOf(ret, count);
	}

	
	static private long twoHoursAgo() {
		return Clock.currentTimeMillis() - TWO_HOURS;
	}

	private void giveOwnershipTo(User newOwner) {
		this._owner = newOwner;
	}

	private boolean isOwner(User user) {
		return owner().equals(user);
	}
	
	@Override
	public void replace(User fromUser, User toUser) {
		if (isOwner(fromUser)) {
			giveOwnershipTo(toUser);
			return;
		}
		
		if (isInvited(fromUser)) 
			addInvitee(toUser);
			
		if (!isInterested(fromUser)) 
			try {
				notInterested(toUser);	
			} catch (Exception e) {} // Do not need to handle exception. It is ok if it happens. 
			
		for (Occurrence occ : occurrences()) 
			occ.copyBehavior(fromUser, toUser);
	}

	@Override
	public boolean isPublic() {
		return publicEvent;
	}

	@Override
	public void setPublic(boolean publicEvent) {
		this.publicEvent = publicEvent;
	}
}
