package guardachuva.agitos.domain;

import guardachuva.agitos.shared.BusinessException;
import guardachuva.agitos.shared.Validations;

import java.io.Serializable;
import java.util.ArrayList;
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
		if (Event.errorsForConstruction(moderator, description, date).length > 0)
			throw new BusinessException("Erros encontrados. Valide antes da criação.");
		
		Event event = new Event(id, moderator, description, date);
		
		return event;
	}
	
	public static String[] errorsForConstruction(User moderator, String description, Date date) {
		ArrayList<String> errors = new ArrayList<String>();
		
		if (moderator == null)
			errors.add("Não foi selecionado um moderador.");
		
		if (!Validations.validateMinLength(description, 3))
			errors.add("A descrição deve possuir no mínimo 3 caracteres.");
		
		if (date == null)
			errors.add("A data ou a hora é inválida.");
		
		return errors.toArray(new String[errors.size()]);
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
