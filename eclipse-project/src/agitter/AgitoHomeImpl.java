package agitter;

import java.util.*;

import agitter.util.AgitoComparator;import agitter.util.Clock;

public class AgitoHomeImpl implements AgitoHome {

	private Clock _clock;
	private SortedSet<Agito> all = new TreeSet<Agito>( new AgitoComparator() );
	public AgitoHomeImpl(Clock _clock) {
		this._clock = _clock;
	}

	@Override
	public Agito create(String description, Date date) {
		Agito agito = new Agito(description, date);
		this.all().add(agito);
		return agito;
	}

	@Override
	public SortedSet<Agito> all() {
		return all;
	}
	@Override
	public SortedSet<Agito> toHappen() {
		return all.tailSet(new Agito("Dummy", this._clock.date()));
	}

}
