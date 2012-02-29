package agitter.domain.events;

import java.util.ArrayList;
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

//	@SuppressWarnings("unused")	@Deprecated transient private long id; //2011-10-19
	private long id; //2011-02-16 - Change to final after schema migration

	final private User _owner;
	private String _description;
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
		
		notInterested.add(user);
	}
	
	@Override
	public void notInterested(User user, long date) {
		if(owner().equals(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		
		for (Occurrence o : actualOccurrences()) {
			if (o.datetime() == date) {
				o.notInterested(user);
			}
		}
	}

	
	
	private boolean isInterested(User user) {
		return !notInterested.contains(user);
	}
	
	
	boolean isVisibleTo(User user) {
		if (owner().equals(user)) return true;
		return isInvited(user) && isInterested(user);
	}


	private boolean isInvited(User user) {
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
		//datetimes = new long[]{newDatetime};
		actualOccurrences().clear();
		addDate(newDatetime);
	}

	void edit(String newDescription, long[] newDatetimes) throws Refusal {
		if (null == newDescription) { throw new Refusal("Descrição do agito deve ser preenchida."); }

		_description = newDescription;
		//datetimes = newDatetimes;
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
        //long[] copy = new long[datetimes.length+1];
        //System.arraycopy(datetimes,0,copy,0,datetimes.length);
        //copy[datetimes.length] = datetime;
        //datetimes = copy;
		actualOccurrences().add(new OccurrenceImpl(datetime));
	}

	private Occurrence searchOccurrence(long datetime) {
		for (Occurrence occ : occurrences) {
			if (occ.datetime() == datetime) return occ;
		}
		return null;
	}
	
	@Override
	public void removeDate(long datetime) {
		// dont remove the last one. 
		//if (datetimes.length == 1) return;
		
		Occurrence toRemove = searchOccurrence(datetime);
		if (toRemove != null)
			actualOccurrences().remove(toRemove);
		
		//for (int i=0; i<datetimes.length; i++) {
		//	if (datetimes[i] == datetime) {
		//       long[] copy = new long[datetimes.length-1];
		//        System.arraycopy(datetimes,0,copy,0,i);
		//        System.arraycopy(datetimes,i+1,copy,i,datetimes.length-(i+1));
		//        datetimes = copy;
		//        return;
	//		}
	//	}
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
	
	@Deprecated
	@Override
	public long getId() {
		return id;
	}
	
	@Deprecated
	@Override
	public void setId(long id) {
		this.id = id;
	}


	@Override
	public long[] interestedDatetimes(User user) {
		// TWO hours ago mesmo na lista? 
		final long twoHoursAgo = Clock.currentTimeMillis() - TWO_HOURS;
		
		List<Long> interesting = new ArrayList<Long>(); 
		for (Occurrence occ : occurrences()) {
			if (occ.datetime() > twoHoursAgo && occ.isInterested(user)) {
				interesting.add(occ.datetime());
			}
		}
		
		long[] ret = new long[interesting.size()];
		int i=0;
		for (Long l : interesting) ret[i++] = l; 
		return ret;
	}

	// TODO: this is a mess.
	@Override
	public long[] datetimesToCome() {
		final long twoHoursAgo = Clock.currentTimeMillis() - TWO_HOURS;
		
		List<Long> futureDates = new ArrayList<Long>();
		
		for (Occurrence occ : occurrences) 
			if (occ.datetime() > twoHoursAgo)
				futureDates.add(occ.datetime());
		
		
		long[] ret = new long[futureDates.size()];
	    int i=0;
	    for (Long data : futureDates) 
	        ret[i++] = data;
	    
	    
		return (long[]) ret;
	}
	
}
