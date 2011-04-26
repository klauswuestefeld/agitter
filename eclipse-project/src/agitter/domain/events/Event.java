package agitter.domain.events;


import java.io.Serializable;

import sneer.foundation.lang.ReadOnly;

public class Event implements Serializable, ReadOnly {

	private static final long serialVersionUID = 1L;

	final private long _datetime;
	final private String _description;

	public Event(String description, long datetime) {
		if (datetime == 0) throw new IllegalArgumentException("Data do agito deve ser preenchida.");
		if (null == description) throw new IllegalArgumentException("description can not be null");
		_description = description;
		_datetime = datetime;
	}

	public String description() {
		return _description;
	}

	public long datetime() {
		return _datetime;
	}

	@Override
	public boolean equals(Object o) {
		if(this==o) { return true; }
		if(o==null || getClass()!=o.getClass()) { return false; }

		Event agito = (Event) o;

		return _datetime == agito._datetime && _description.equals(agito._description);
	}

	@Override
	public int hashCode() {
		int result = (int)_datetime;
		result = 31*result+_description.hashCode();
		return result;
	}

}
