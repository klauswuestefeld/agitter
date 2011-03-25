package guardachuva.agitos.server.domain;

import guardachuva.agitos.shared.BusinessException;
import guardachuva.agitos.shared.EventDTO;
import guardachuva.agitos.shared.ValidationException;

import java.io.Serializable;
import java.util.Date;

public final class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int _id;
	private final User _moderator;
	private final String _description;
	private final long _date;
	
	private Event(int id, User moderator, String description, Date date) {
		this._id = id;
		this._moderator = moderator;
		this._description = description;
		this._date = date.getTime();
	}
	
	public int getId() {
		return _id;
	}

	public User getModerator() {
		return _moderator;
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
		
		Event event = new Event(id, moderator, description, date);
		
		return event;
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
				+ ((_moderator == null) ? 0 : _moderator.hashCode());
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
		if (_moderator == null) {
			if (other._moderator != null)
				return false;
		} else if (!_moderator.equals(other._moderator))
			return false;
		return true;
	}
	
}
