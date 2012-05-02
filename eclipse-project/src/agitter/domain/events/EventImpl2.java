package agitter.domain.events;

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

	@Deprecated private User _owner;
	@Deprecated private Set<Group> groupInvitations = new HashSet<Group>();
	@Deprecated private Set<User> invitees = new HashSet<User>();
	
	private Invitation invitationTree;
	
	private String _description;
	@SuppressWarnings("unused") @Deprecated private long _datetime;
	@SuppressWarnings("unused") @Deprecated private long[] datetimes;
	
	private Set<Occurrence> occurrences = new HashSet<Occurrence>();
	
	final private Set<User> notInterested = new HashSet<User>();
	
	public EventImpl2(long id, User owner, String description, long datetime) throws Refusal {
		this.id = id;
		if(null==owner) { throw new IllegalArgumentException("user cannot be null"); }
		_owner = owner;
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
		for (Occurrence occ : actualOccurrences()) {
			ret[cont++] = occ.datetime();
		}
		return ret;
	}

	@Override
	public Occurrence[] occurrences() {
		return actualOccurrences().toArray(new Occurrence[actualOccurrences().size()]);
	}
	
	private Set<Occurrence> actualOccurrences() {
		if (occurrences==null) occurrences = new HashSet<Occurrence>();
		return occurrences;
	}
	
	@Deprecated
	synchronized
	public User[] invitees() {
		return actualInvitees().toArray(new User[actualInvitees().size()]);
	}

	@Deprecated
	private Set<User> actualInvitees() {
		if (invitees==null) invitees = new HashSet<User>();
		return invitees;
	}

	@Deprecated
	synchronized
	public Group[] groupInvitees() {
		return actualGroupInvitees().toArray(new Group[actualGroupInvitees().size()]);
	}


	@Deprecated
	private Set<Group> actualGroupInvitees() {
		if (groupInvitations==null) groupInvitations = new HashSet<Group>();
		return groupInvitations;
	}


	@Override
	public void notInterested(User user) {
		if(isOwner(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		if(!isInvited(user)) throw new IllegalArgumentException( "Não convidados não podem deixar de se interessar." );
		
		notInterested.add(user);
	}
	
	@Override
	public void notInterested(User user, long date) {
		if(isOwner(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		else if(!isInvited(user)) throw new IllegalArgumentException( "Não convidados não podem deixar de se interessar." );

		Occurrence o = searchOccurrence(date);
		if (o != null) {
			o.notInterested(user);
		}
	}
	
	@Override
	public void going(User user, long date) {
		Occurrence o = searchOccurrence(date);
		if(!isOwner(user) && !isInvited(user)) throw new IllegalArgumentException( "Não convidados não podem participar." );
		
		if (o != null) {
			o.going(user);
		}
	}

	@Override
	public void notGoing(User user, long date) {
		if(isOwner(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		else if(!isInvited(user)) throw new IllegalArgumentException( "Não convidados não podem deixar de se interessar." );

		Occurrence o = searchOccurrence(date);
		if (o != null) {
			o.notGoing(user);
		}
	}

	@Override
	public void mayGo(User user, long date) {
		if(isOwner(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		else if(!isInvited(user)) throw new IllegalArgumentException( "Não convidados não podem deixar de se interessar." );

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
		if (isOwner(user)) return true;
		return isInvited(user) && isInterested(user);
	}

	
	private boolean isInterested(User user) {
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

	void edit(String newDescription, long[] newDatetimes) throws Refusal {
		if (null == newDescription) { throw new Refusal("Descrição do agito deve ser preenchida."); }

		_description = newDescription;
		actualOccurrences().clear();
		for (long l : newDatetimes) {
			addDate(l);
		}
	}
	
	@Override
	public void invite(User host, User invitee) {
		if (!invitationTree.invite(host, invitee)) {
			throw new IllegalArgumentException("Host " + host + " não é convidado");
		}
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
	
	@Deprecated
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

	@Deprecated
	public void addInvitee(Group invitee) {
		actualGroupInvitees().add(invitee);
	}

	@Deprecated
	public void removeInvitee(Group invitee) {
		actualGroupInvitees().remove(invitee);
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
	public long[] datetimesInterestingFor(User user) {
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
		invitationTree.transferHostTo(newOwner);
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
		
		User u = invitationTree.isInvitedBy(fromUser);
		if (u != null) 
			invite(u, toUser);
			
		if (!isInterested(fromUser)) 
			try {
				notInterested(toUser);	
			} catch (Exception e) {} // Do not need to handle exception. It is ok if it happens. 
			
		for (Occurrence occ : occurrences()) 
			occ.copyBehavior(fromUser, toUser);
	}

	void migrateSchemaIfNecessary() {
		// 2012-May-01
		if (invitationTree == null) {
			invitationTree = new InvitationImpl(_owner);
			((InvitationImpl)invitationTree).inviteAllUsers(_owner, invitees);
			((InvitationImpl)invitationTree).inviteAllGroups(_owner, groupInvitations);
		}
	}

	@Override
	public Invitation invitationTree() {
		return invitationTree;
	}
	
	@Override
	public String toString() {
		return description();
	}
}
