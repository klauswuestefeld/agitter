package agitter.server.domain;


import java.io.Serializable;
import java.util.Date;

import agitter.shared.BusinessException;
import agitter.shared.EventDTO;
import agitter.shared.ValidationException;

public final class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int _id;
	private final User _owner;
	private final String _description;
	private final long _date;
	
	private Event(int id, User owner, String description, long datetime) {
		this._id = id;
		this._owner = owner;
		this._description = description;
		this._date = datetime;
	}
	
	public int getId() {
		return _id;
	}

	public User getModerator() {
		return _owner;
	}

	public String getDescription() {
		return _description;
	}

	public Date getDate() {
		return new Date(_date);
	}

	public static Event createFor(int id, User moderator, String description, Date date) throws BusinessException {
		String[] errors = EventDTO.errorsForConstruction(moderator.getEmail(), description, date);
		if (errors.length > 0)
			throw new ValidationException(errors);

		return new Event(id, moderator, description, date.getTime());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (_date ^ (_date >>> 32));
		result = prime * result
				+ ((_description == null) ? 0 : _description.hashCode());
		result = prime * result + _id;
		result = prime * result
				+ ((_owner == null) ? 0 : _owner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (_date != other._date)
			return false;
		if (_description == null) {
			if (other._description != null)
				return false;
		} else if (!_description.equals(other._description))
			return false;
		if (_id != other._id)
			return false;
		if (_owner == null) {
			if (other._owner != null)
				return false;
		} else if (!_owner.equals(other._owner))
			return false;
		return true;
	}
	
}
